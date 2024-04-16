package com.example.problem_solvingapp.data;

public class Tag {
    private int TagID;
    private String TagName;
    public int getID(){
        return TagID;
    }
    public String getName(){
        return TagName;
    }

    public Tag(int id,String name){
        this.TagID = id;
        this.TagName = name;
    }

}
