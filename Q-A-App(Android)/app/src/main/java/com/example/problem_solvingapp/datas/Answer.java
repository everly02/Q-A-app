package com.example.problem_solvingapp.datas;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Answer {
    @SerializedName("AnswerID")
    private int answererID;

    @SerializedName("content")
    private String content;

    @SerializedName("Timestamp")
    private Date timestamp;

    public Answer(int answererID, String content){
        this.answererID=answererID;
        this.content=content;
    }
}
