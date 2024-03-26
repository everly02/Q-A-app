const express = require('express');
const mysql = require('mysql2');
const { OpenAI } = require('openai');
const app = express();
const port = 3000;

const db = mysql.createConnection({
    host: 'localhost',
    user: 'u0', // 替换为您的 MySQL 用户名
    password: '717274', // 替换为您的 MySQL 密码
    database: 'main'
  });
const { Configuration, OpenAIApi } = require("openai");

const configuration = new Configuration({
  apiKey: process.env.OPENAI_API_KEY, // 使用环境变量中的 API 密钥
});
const openai = new OpenAIApi(configuration);

  db.connect(err => {
    if (err) {
        console.error('Error connecting to the database: ', err);
        return;
    }
    console.log('Connected to the database.');
});

// 使 express 能够解析 JSON 格式的请求体
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
    
    try {
        const response = await openai.createCompletion({
            model: "text-davinci-003", // 指定使用的模型，确保使用最适合的模型
            prompt: `Q: ${inputString}\nA:`, // 构造一个提示，明确表示这是一个问题并需要回答
            temperature: 0.7, // 设置温度参数，以控制输出的创新性
            max_tokens: 150, // 设置生成文本的最大长度
            stop: ["\n"], // 设置停止符，以便在得到一个答案后停止
        });

        const processedString = response.data.choices[0].text.trim(); // 获取处理后的字符串，即回答
        res.json({ answer: processedString }); // 返回处理后的字符串作为回答
    } catch (error) {
        console.error("Error calling OpenAI API:", error);
        res.status(500).json({ error: "Failed to process string with OpenAI API" });
    }
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
    db.query('SELECT QuestionID, Title FROM Questions', (err, rows) => {
        if (err) {
            res.status(500).json({ error: err.message });
            console.log(err.message);
            return;
        }
        // 返回问题标题列表给客户端
        res.json({ questions: rows });
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
        res.json({ question: row });
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