package com.example.problem_solvingapp.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Answer {

    @SerializedName("AnswerID")
    @Expose
    private int answerId;

    @SerializedName("Content")
    @Expose
    private String content;

    @SerializedName("Timestamp")
    @Expose
    private String timestamp; // Consider using a Date object with proper deserialization

    @SerializedName("approves")
    @Expose
    private int approves;

    @SerializedName("AnswererID")
    @Expose
    private int userId;

    @SerializedName("Username")
    @Expose
    private String username; // Answerer's username

    @SerializedName("avatar")
    @Expose
    private String avatar;
    public Answer(int answerId, String content, String timestamp, int approves, int userId, String username, String avatar) {
        this.answerId = answerId;
        this.content = content;
        this.timestamp = timestamp;
        this.approves = approves;
        this.userId = userId;
        this.username = username;
        this.avatar = avatar;
    }

    // Getters and Setters
    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getApproves() {
        return approves;
    }

    public void setApproves(int approves) {
        this.approves = approves;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
