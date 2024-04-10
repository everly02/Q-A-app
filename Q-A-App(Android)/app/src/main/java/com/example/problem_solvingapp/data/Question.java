package com.example.problem_solvingapp.data;

public class Question {
    private int questionId;
    public String title;
    public int approves;
    public int views;
    private String timestamp; // Consider using a Date object with proper deserialization
    public int reviews; // This might be an array of Review objects if you decide to fetch them together
    public String username; // Asker's username
    private String avatar;

    public Question(int questionId, String title, int approves, int views, String timestamp, int reviews, String username, String avatar) {
        this.questionId = questionId;
        this.title = title;
        this.approves = approves;
        this.views = views;
        this.timestamp = timestamp;
        this.reviews = reviews;
        this.username = username;
        this.avatar = avatar;
    }
}
