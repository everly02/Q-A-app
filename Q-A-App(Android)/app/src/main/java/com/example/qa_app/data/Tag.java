package com.example.qa_app.data;

import java.util.Optional;

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
        this.TagName = Optional.ofNullable(name).orElse("DefaultTag");
    }

}
