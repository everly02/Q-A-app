package com.example.problem_solvingapp.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionDetail {
    @SerializedName("question")
    @Expose
    private detailed_ques question;

    @SerializedName("reviews")
    @Expose
    private List<Review> reviews;

    @SerializedName("answers")
    @Expose
    private List<Answer> answers;

    @SerializedName("tags")
    @Expose
    private List<Tag> tags;
    public QuestionDetail(detailed_ques question, List<Review> reviews, List<Answer> answers, List<Tag> tags) {
        this.question = question;
        this.reviews = reviews;
        this.answers = answers;
        this.tags = tags;
    }

    // Getters
    public detailed_ques getQuestion() {
        return question;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public List<Tag> getTags(){
        return tags;
    }

    // Setters
    public void setQuestion(detailed_ques question) {
        this.question = question;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }



}
