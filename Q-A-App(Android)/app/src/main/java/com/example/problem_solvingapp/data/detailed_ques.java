package com.example.problem_solvingapp.data;

import com.google.gson.annotations.SerializedName;

public class detailed_ques {
    @SerializedName("QuestionID")
    private int questionId;
    @SerializedName("Title")
    public String title;
    @SerializedName("approves")
    public int approves;
    @SerializedName("views")
    public int views;
    @SerializedName("Timestamp")
    private String timestamp; //
    @SerializedName("reviews")
    public int reviews; // This might be an array of Review objects if you decide to fetch them together
    @SerializedName("UserID")
    private int AskerID;
    @SerializedName("Username")
    public String username; // Asker's username
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("Content")
    private String content;
    public String getTitle() {
        return title;
    }
    public int getApproves() {
        return approves;
    }
    public int getViews() {
        return views;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public int getReviews() {
        return reviews;
    }
    public String getUsername() {
        return username;
    }
    public String getAvatar() {
        return avatar;
    }
    public int getAskerID() {
        return AskerID;
    }
    public String getContent() {
        return content;
    }
}
