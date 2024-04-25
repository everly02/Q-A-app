package com.example.qa_app.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class QuestionDetail {
    @SerializedName("QuestionTitle")
    private final String questionTitle;
    @SerializedName("QuestionContent")
    private final String questionContent;

    @SerializedName("Approves")
    private final int approves;
    @SerializedName("Views")
    private final int views;

    @SerializedName("Answers")
    private List<Answer> answers;
    @SerializedName("Reviews")
    private List<Review> reviews;
    @SerializedName("Tags")
    private List<Tag> tags;
    public QuestionDetail(String questionTitle,String questionContent, int approves,int views,List<Review> reviews, List<Answer> answers, List<Tag> tags) {
        this.approves=approves;
        this.views = views;
        this.questionTitle = questionTitle;

        this.questionContent = questionContent;
        this.reviews = Optional.ofNullable(reviews).orElse(Collections.emptyList());
        this.answers = Optional.ofNullable(answers).orElse(Collections.emptyList());
        this.tags = Optional.ofNullable(tags).orElse(Collections.emptyList());
    }

    // Getters
    public String getQuestionTitle() {
        return questionTitle;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public int getApproves(){ return approves; }
    public int getViews(){return views;}
    public List<Review> getReviews() {
        return reviews;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public List<Tag> getTags(){
        return tags;
    }


    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

}
