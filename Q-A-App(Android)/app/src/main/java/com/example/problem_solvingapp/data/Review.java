package com.example.problem_solvingapp.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("content")
    @Expose
    private String content;

    public Review(String struct_content){
        this.content = struct_content;
    }

    public String getContent() {
        return content;
    }
}
