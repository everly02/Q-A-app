package com.example.qa_app.data;

public class NewQuestion {
    private int AskerID;
    private String Title;
    private String Content;

    public NewQuestion(int askerId, String title, String content) {
        this.AskerID = askerId;
        this.Title = title;
        this.Content = content;
    }
    // Constructor, getters, and setters...
}