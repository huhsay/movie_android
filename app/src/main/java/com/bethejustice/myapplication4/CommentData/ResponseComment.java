package com.bethejustice.myapplication4.CommentData;

import java.util.ArrayList;

public class ResponseComment {

    public String message;
    public int code;
    public String resultType;
    public ArrayList<Comment> result  = new ArrayList<>();

    public ArrayList<Comment> getResult(){
        return result;
    }
}
