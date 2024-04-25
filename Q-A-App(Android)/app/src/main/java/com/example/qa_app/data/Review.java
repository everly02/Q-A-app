package com.example.qa_app.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Optional;

public class Review {
    @SerializedName("ReviewContent")
    @Expose
    private String content;

    public Review(String struct_content){
        this.content = Optional.ofNullable(content).orElse("No review content");
    }

    public String getContent() {
        return content;
    }
}
