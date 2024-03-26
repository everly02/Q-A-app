package com.example.problem_solvingapp;
import com.google.gson.annotations.SerializedName;
public class QuestionDetail {
    @SerializedName("TITLE")
    private String title;
    @SerializedName("CONTENT")
    private String content;
    public QuestionDetail(String title, String content){
        this.title=title;
        this.content=content;
    }
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

}
