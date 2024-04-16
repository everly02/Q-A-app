package com.example.problem_solvingapp.data;

import com.google.gson.annotations.SerializedName;

public class Question {
        @SerializedName("QuestionID")
        private int questionId;
        @SerializedName("Title")
        public String title;
        @SerializedName("approves")
        public int approves;
        @SerializedName("views")
        public int views;
        @SerializedName("Timestamp")
        private String timestamp; // Consider using a Date object with proper deserialization
        @SerializedName("reviews")
        public int reviews; // This might be an array of Review objects if you decide to fetch them together
        @SerializedName("UserID")
        private int AskerID;
        @SerializedName("Username")
        public String username; // Asker's username
        @SerializedName("avatar")
        public String avatar;

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
    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getApproves() {
        return approves;
    }

    public void setApproves(int approves) {
        this.approves = approves;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public int getAskerID() {
        return AskerID;
    }

    public void setAskerID(int askerID) {
        AskerID = askerID;
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
