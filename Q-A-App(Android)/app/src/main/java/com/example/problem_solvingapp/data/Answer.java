package com.example.problem_solvingapp.data;

public class Answer {
    private int answerId;
    private String content;
    private String timestamp; // Consider using a Date object with proper deserialization
    private int approves;
    private String username; // Answerer's username
    private String avatar; // Answerer's avatar URL
    // Constructor, getters, and setters...
}
