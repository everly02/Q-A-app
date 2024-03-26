package com.example.problem_solvingapp;

import com.google.gson.annotations.SerializedName;

public class li_item {
    @SerializedName("QuestionID")
    private int questionID;
    @SerializedName("Title")
    private String title;

    public int getQuestionID() {
        return questionID;
    }

    public String getTitle() {
        return title;
    }
    public li_item(int id, String title){
        this.questionID = id;
        this.title = title;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
