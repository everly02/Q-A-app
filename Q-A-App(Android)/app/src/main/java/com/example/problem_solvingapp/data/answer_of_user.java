package com.example.problem_solvingapp.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class answer_of_user {
    @SerializedName("QuestionID")
    private int qid;
    @SerializedName("answerId")
    @Expose
    private int answerId;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("timestamp")
    @Expose
    private String timestamp; // Consider using a Date object with proper deserialization

    @SerializedName("approves")
    @Expose
    private int approves;

    @SerializedName("userId")
    @Expose
    private int userId;

    private String QuestionTitle;

    public answer_of_user(int qid, int answerId, String content, String timestamp, int approves, int userId, String questionTitle) {
        this.qid = qid;
        this.answerId = answerId;
        this.content = content;
        this.timestamp = timestamp;
        this.approves = approves;
        this.userId = userId;
        this.QuestionTitle = questionTitle;
    }
    public String getContent(){
        return content;
    }

    public String getQuestionTitle() {
        return QuestionTitle;
    }

    public int getqid(){
        return qid;
    }
}
