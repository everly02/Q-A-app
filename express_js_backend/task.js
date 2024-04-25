const express = require('express');
const mysql = require('mysql2');
const multer = require('multer');
const bcrypt = require('bcrypt');
const path = require('path');
const morgan = require('morgan');
const app = express();

const { GoogleGenerativeAI } = require("@google/generative-ai");


const genAI = new GoogleGenerativeAI(process.env.API_KEY);
const router = express.Router();
app.use(express.json());
app.use(morgan('combined'));
const db = mysql.createConnection({
    host: 'localhost', // or your database server's address
    user: 'u0',
    password: '717274',
    database: 'main',
});
db.connect((err) => {
    if (err) {
      console.error('连接数据库失败：', err);
      return;
    }
    console.log('成功连接到数据库');
  });

const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, 'avatars'); // 保存的路径
    },
    filename: (req, file, cb) => {
        const username = req.body.username;
        const ext = path.extname(file.originalname); // 获取文件扩展名
        cb(null, `${username}${ext}`); // 使用用户名作为文件名
    }
});

const upload = multer({ storage: storage });

app.post('/process-string', async (req, res) => {
    	const { inputString } = req.body; // 从请求体中获取输入的提问字符串
    	const model = genAI.getGenerativeModel({ model: "gemini-pro"});

	const prompt =inputString;
	const result = await model.generateContent(prompt);
  	const response = await result.response;
    	
        const processedString = response.text();
	res.json({ answer: processedString }); // 返回处理后的字符串作为回答
    });

router.get('/questions', (req, res) => {
    const sql = `
    SELECT q.QuestionID, q.Title, q.approves, q.views, q.Timestamp, q.reviews,
            u.Username, u.avatar, u.UserID
    FROM Questions q
    JOIN Users u ON q.AskerID = u.UserID`;

    db.query(sql, (error, results) => {
        if (error) return res.status(500).json({ error });
        res.json({ questions: results });
    });
});
app.get('/questions/:id', (req, res) => {
    const questionId = req.params.id;
  
    // 构造SQL查询语句
    const sqlQuery = `
      SELECT q.Title AS QuestionTitle, q.Content AS QuestionContent, q.approves AS Approves, q.views AS Views,
             GROUP_CONCAT(DISTINCT a.Content) AS AnswerContent,
             GROUP_CONCAT(DISTINCT a.approves) AS AnswerApproves,
             GROUP_CONCAT(DISTINCT u.Username) AS AnswererUsername,
             GROUP_CONCAT(DISTINCT u.avatar) AS AnswererAvatar,
             GROUP_CONCAT(DISTINCT r.Content) AS ReviewContent,
             GROUP_CONCAT(DISTINCT t.TagName) AS Tags
      FROM Question q
      LEFT JOIN Answers a ON q.QuestionID = a.QuestionID
      LEFT JOIN Users u ON a.AnswererID = u.UserID
      LEFT JOIN Reviews r ON q.QuestionID = r.QuestionID
      LEFT JOIN Tags t ON t.QuestionID = q.QuestionID
      WHERE q.QuestionID = ?
      GROUP BY q.QuestionID
    `;
  
    // 执行查询
    connection.query(sqlQuery, [questionId], (error, results) => {
      if (error) {
        console.error('Error querying database:', error);
        res.status(500).json({ error: 'Internal Server Error' });
        return;
      }
  
      
      const formattedResults = results.map(result => {
        return {
          QuestionTitle: result.QuestionTitle,
          QuestionContent: result.QuestionContent,
            Approves: result.approves,
            Views: result.views,
          Answers: result.AnswerContent.split(','), 
          AnswerApproves: result.AnswerApproves.split(','), 
          Answerers: result.AnswererUsername.split(','), 
          AnswererAvatars: result.AnswererAvatar.split(','), 
          Reviews: result.ReviewContent.split(','), 
          Tags: result.Tags.split(',') 
        };
      });
  
      
      res.json(formattedResults);
    });
  });
router.get('/users/:id/questions', (req, res) => {
    const userId = req.params.id;
    const sql = `
    SELECT * FROM Questions WHERE AskerID = ?`;

    db.query(sql, [userId], (error, questions) => {
        if (error) return res.status(500).json({ error });
        res.json({ questions });
    });
});

router.get('/users/:id/answers', (req, res) => {
    const userId = req.params.id;
    const sql = `
    SELECT a.*, q.Title AS QuestionTitle
    FROM Answers a
    JOIN Questions q ON a.QuestionID = q.QuestionID
    WHERE a.AnswererID = ?`;

    db.query(sql, [userId], (error, answers) => {
        if (error) return res.status(500).json({ error });
        res.json({ answers });
    });
});
router.get('/questions/by-tags', (req, res) => {
    // Extract tags from query parameter and split into array
    const tags = req.query.tags ? req.query.tags.split(',') : [];

    if (tags.length === 0) {
        return res.status(400).json({ error: 'At least one tag is required' });
    }

    // Generate placeholders for prepared statement based on number of tags
    const placeholders = tags.map(() => '?').join(',');

    // SQL query to find questions associated with any of the specified tags
    const sql = `
    SELECT DISTINCT q.QuestionID, q.Title, q.approves, q.views, q.Timestamp, q.reviews,
                    u.Username, u.avatar, u.UserID
    FROM Questions q
    JOIN QuestionTags qt ON q.QuestionID = qt.QuestionID
    JOIN Tags t ON qt.TagID = t.TagID
    JOIN Users u ON q.AskerID = u.UserID
    WHERE t.TagName IN (${placeholders})
    ORDER BY q.Timestamp DESC`;

    db.query(sql, tags, (error, results) => {
        if (error) return res.status(500).json({ error });
        res.json({ questions: results });
    });
});
router.post('/questions', (req, res) => {
    const { AskerID, Title, Content } = req.body;

    if (!AskerID || !Title || !Content) {
        return res.status(400).json({ error: 'Missing required fields' });
    }

    const sql = `INSERT INTO Questions (AskerID, Title, Content) VALUES (?, ?, ?)`;

    db.query(sql, [AskerID, Title, Content], (error, results) => {
        if (error) return res.status(500).json({ error });
        res.json({ message: 'Question added successfully', QuestionID: results.insertId });
    });
});

router.post('/questions/:id/comments', (req, res) => {
    const QuestionID = req.params.id;
    const { Content } = req.body;

    if (!Content) {
        return res.status(400).json({ error: 'Comment content is required' });
    }

    const sql = `INSERT INTO Reviews (QuestionID, Content) VALUES (?, ?)`;

    db.query(sql, [QuestionID, Content], (error, results) => {
        if (error) return res.status(500).json({ error });
        res.json({ message: 'Comment added successfully', ReviewID: results.insertId });
    });
});
router.post('/questions/:id/answers', (req, res) => {
    const QuestionID = req.params.id;
    const { AnswererID, Content } = req.body;

    if (!AnswererID || !Content) {
        return res.status(400).json({ error: 'Missing required fields for answer' });
    }

    const sql = `INSERT INTO Answers (QuestionID, AnswererID, Content) VALUES (?, ?, ?)`;

    db.query(sql, [QuestionID, AnswererID, Content], (error, results) => {
        if (error) return res.status(500).json({ error });
        res.json({ message: 'Answer added successfully', AnswerID: results.insertId });
    });
});
router.patch('/questions/:id/approves', (req, res) => {
    const QuestionID = req.params.id;
    const { action } = req.body; // 'increment' or 'decrement'

    if (!['increment', 'decrement'].includes(action)) {
        return res.status(400).json({ error: 'Invalid action specified' });
    }

    const sql = `
        UPDATE Questions
        SET approves = approves ${action === 'increment' ? '+' : '-'} 1
        WHERE QuestionID = ?
    `;

    db.query(sql, [QuestionID], (error, results) => {
        if (error) return res.status(500).json({ error });
        res.json({ message: `Question approves successfully ${action === 'increment' ? 'incremented' : 'decremented'}` });
    });
});
router.patch('/answers/:id/approves', (req, res) => {
    const AnswerID = req.params.id;
    const { action } = req.body; // 'increment' or 'decrement'

    if (!['increment', 'decrement'].includes(action)) {
        return res.status(400).json({ error: 'Invalid action specified' });
    }

    const sql = `
        UPDATE Answers
        SET approves = approves ${action === 'increment' ? '+' : '-'} 1
        WHERE AnswerID = ?
    `;

    db.query(sql, [AnswerID], (error, results) => {
        if (error) return res.status(500).json({ error });
        res.json({ message: `Answer approves successfully ${action === 'increment' ? 'incremented' : 'decremented'}` });
    });
});
router.patch('/questions/:id/views', (req, res) => {
    const { QuestionID } = req.params;
  if (!QuestionID) {
    return res.status(400).send('QuestionID is required');
  }

  // SQL 更新语句
  const query = 'UPDATE Questions SET views = views + 1 WHERE QuestionID = ?';

  db.query(query, [QuestionID], (error, results) => {
    if (error) {
      return res.status(500).send('Error updating views count');
    }
    if (results.affectedRows === 0) {
      return res.status(404).send('Question not found');
    }
    res.status(200).send('Views incremented successfully');
  });
});
router.delete('/questions/:id', (req, res) => {
    const QuestionID = req.params.id;

    // Start a transaction to ensure all related data is deleted atomically
    db.beginTransaction(err => {
        if (err) { return res.status(500).json({ error: err.message }); }

        // Delete answers related to the question
        const deleteAnswers = `DELETE FROM Answers WHERE QuestionID = ?`;
        db.query(deleteAnswers, [QuestionID], (error) => {
            if (error) {
                return db.rollback(() => {
                    res.status(500).json({ error: error.message });
                });
            }

            // Delete reviews related to the question
            const deleteReviews = `DELETE FROM Reviews WHERE QuestionID = ?`;
            db.query(deleteReviews, [QuestionID], (error) => {
                if (error) {
                    return db.rollback(() => {
                        res.status(500).json({ error: error.message });
                    });
                }

                // Delete the question itself
                const deleteQuestion = `DELETE FROM Questions WHERE QuestionID = ?`;
                db.query(deleteQuestion, [QuestionID], (error) => {
                    if (error) {
                        return db.rollback(() => {
                            res.status(500).json({ error: error.message });
                        });
                    }

                    // If all deletions were successful, commit the transaction
                    db.commit(err => {
                        if (err) {
                            return db.rollback(() => {
                                res.status(500).json({ error: err.message });
                            });
                        }
                        res.json({ message: 'Question and related data successfully deleted' });
                    });
                });
            });
        });
    });
});
router.delete('/answers/:id', (req, res) => {
    const AnswerID = req.params.id;

    const sql = `DELETE FROM Answers WHERE AnswerID = ?`;

    db.query(sql, [AnswerID], (error, results) => {
        if (error) return res.status(500).json({ error });
        if (results.affectedRows === 0) {
            return res.status(404).json({ message: 'Answer not found' });
        }
        res.json({ message: 'Answer successfully deleted' });
    });
});
router.post('/register', upload.single('avatar'),async (req, res) => {
    const { username, password, email } = req.body;
    
    const avatarURI = `${req.protocol}://${req.get('host')}/avatars/${req.file.filename}`;
    const hashedPassword = await bcrypt.hash(password, 10);
    const query = 'INSERT INTO Users (Username, Password, Email, avatar) VALUES (?, ?, ?, ?)';
    db.query(query, [username, hashedPassword, email, avatarURI], (error, results) => {
        if (error) {
            // 捕捉重名错误
            if (error.code === 'ER_DUP_ENTRY') {
                res.status(409).send('Username already exists');
            } else {
                console.error('Database Insertion Error: ', error);
                res.status(500).send('Database error');
            }
            return;
        }
        res.send('Registration successful');
    });
});

// Login Endpoint
router.post('/login', async (req, res) => {
    const { username, password } = req.body;

    if (!username || !password) {
        return res.json({ id:-10});
    }

    try {
        const [user] = await db.promise().query(`SELECT * FROM Users WHERE Username = ?`, [username]);
        if (!user.length) {
            return res.json({ id:-20});
        }

        const isMatch = await bcrypt.compare(password, user[0].Password);
        if (!isMatch) {
            return res.json({ success:"-30"});
        }

        // Returning true for successful login
        res.json({ id: user[0].UserID });
    } catch (error) {
        console.error(error);
        res.status(500).send('Server error');
    }
});
app.post('/questions/:id/view', (req, res) => {
    const questionId = req.params.id;
    const query = `UPDATE Questions SET views = views + 1 WHERE QuestionID = ?`;

    db.execute(query, [questionId], (error, results) => {
        if (error) {
            return res.status(500).json({ error: "Database error" });
        }
        res.json({ message: "View count updated successfully", affectedRows: results.affectedRows });
    });
});
router.get('/tags', (req, res) => {
    const query = 'SELECT TagID, TagName FROM Tags';
    db.query(query, (err, results) => {
        if (err) {
            res.status(500).send('数据库查询失败');
            console.error(err);
        } else {
            res.status(200).json(results);
        }
    });
});

// 路由：给定问题 ID 和标签 ID，添加对应关系
router.post('/questiontags', (req, res) => {
    const { QuestionID, TagID } = req.body;
    const query = 'INSERT INTO QuestionTags (QuestionID, TagID) VALUES (?, ?)';
    db.query(query, [QuestionID, TagID], (err, result) => {
        if (err) {
            res.status(500).send('添加对应关系失败');
            console.error(err);
        } else {
            res.status(200).send('对应关系添加成功');
        }
    });
});
// Start the server

app.use('/', router);
const port = 3000;
app.listen(port, () => {
    console.log(`App listening at http://localhost:${port}`);
});
