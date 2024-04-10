const express = require('express');
const mysql = require('mysql2');
const bodyParser = require('body-parser');
const apiRoutes = require('./routes/apiRoutes');
const app = express();
const port = 3000;
const { GoogleGenerativeAI } = require("@google/generative-ai");

const genAI = new GoogleGenerativeAI(process.env.API_KEY);
const router = express.Router();
app.use(express.json());

const db = mysql.createConnection({
    host: 'localhost', // or your database server's address
    user: 'u0',
    password: '717274',
    database: 'main',
});

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
router.get('/questions/:id', (req, res) => {
    const questionId = req.params.id;
    // Assuming MySQL 8.0+ for CTE usage for cleaner SQL
    const sql = `
    WITH QuestionDetails AS (
        SELECT * FROM Questions WHERE QuestionID = ?
    ), ReviewContents AS (
        SELECT Content FROM Reviews WHERE QuestionID = ?
    ), AnswerDetails AS (
        SELECT a.*, u.Username, u.avatar, u.UserID
        FROM Answers a
        JOIN Users u ON a.AnswererID = u.UserID
        WHERE a.QuestionID = ?
    ), TagDetails AS (
        SELECT t.TagName
        FROM QuestionTags qt
        JOIN Tags t ON qt.TagID = t.TagID
        WHERE qt.QuestionID = ?
    )
    SELECT * FROM QuestionDetails;
    SELECT Content FROM ReviewContents;
    SELECT * FROM AnswerDetails;
    SELECT TagName FROM TagDetails;`;

    db.query(sql, [questionId, questionId, questionId, questionId], (error, results) => {
        if (error) return res.status(500).json({ error });
        res.json({
            question: results[0],
            reviews: results[1],
            answers: results[2],
            tags: results[3]
        });
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

app.listen(port, () => {
    console.log(`App listening at http://localhost:${port}`);
});