const express = require('express');
const mysql = require('mysql2');
const app = express();
const port = 3000;
const { GoogleGenerativeAI } = require("@google/generative-ai");

const genAI = new GoogleGenerativeAI(process.env.API_KEY);
const db = mysql.createConnection({
    host: 'localhost',
    user: 'u0', 
    password: '717274', 
    database: 'main'
  });


  db.connect(err => {
    if (err) {
        console.error('Error connecting to the database: ', err);
        return;
    }
    console.log('Connected to the database.');
});


app.use(express.json());
//客户端提出新问题
app.post('/ask', (req, res) => {
    handleAskRequest(req, res, db);
    console.log(req.body);
});

// 处理客户端请求浏览问题
app.get('/questions', (req, res) => {
    handlebrowseRequest(req, res, db);
    console.log(req.body);
});

// 处理客户端加载特定问题内容的请求
app.get('/question/:questionID', (req, res) => {
    handleQuestionRequest(req, res, db);
});

// 处理客户端回答问题的请求
app.post('/answer/:questionID', (req, res) => {
    handleAnswerRequest(req, res, db);
});
// 添加一个新的路由以处理字符串
app.post('/process-string', async (req, res) => {
    	const { inputString } = req.body; // 从请求体中获取输入的提问字符串
    	const model = genAI.getGenerativeModel({ model: "gemini-pro"});

	const prompt =inputString;
	const result = await model.generateContent(prompt);
  	const response = await result.response;
    	
        const processedString = response.text();
	res.json({ answer: processedString }); // 返回处理后的字符串作为回答
    });


function handleAskRequest(req, res, db) {
    const { title, content, askerID } = req.body;

    // 插入新问题到数据库
    db.execute('INSERT INTO Questions (Title, Content, AskerID) VALUES (?, ?, ?)', [title, content, askerID], function(err) {
        if (err) {
            res.status(500).json({ error: err.message });
            console.log(err.message);
            return;
        }
        // 返回成功响应
        res.json({ message: 'Question asked successfully', questionID: this.lastID });
        console.log("success");
    });
}

function handlebrowseRequest(req, res, db) {
    db.query('SELECT QuestionID, Title, Timestamp, likes, views FROM Questions', (err, rows) => {
        if (err) {
            res.status(500).json({ error: err.message });
            console.log(err.message);
            return;
        }
        // 返回问题标题列表给客户端
        res.json(rows);
    });
}

function handleQuestionRequest(req, res, db) {
    const questionID = req.params.questionID;

    // 查询特定问题内容
    db.query('SELECT TITLE, CONTENT FROM Questions WHERE QuestionID = ?', [questionID], (err, row) => {
        if (err) {
            res.status(500).json({ error: err.message });
            return;
        }
        if (!row) {
            res.status(404).json({ error: 'Question not found' });
            return;
        }
        res.json(row);
    });
}

function handleAnswerRequest(req, res, db) {
    const questionID = req.params.questionID;
    const { answererID, content } = req.body;

    db.execute('INSERT INTO Answers (QuestionID, AnswererID, Content) VALUES (?, ?, ?)', [questionID, answererID, content], function(err) {
        if (err) {
            res.status(500).json({ error: err.message });
            return;
        }
        
        res.json({ message: 'Answer submitted successfully', answerID: this.lastID });
    });
}
app.listen(port, () => {
    console.log(`App listening at http://localhost:${port}`);
});
