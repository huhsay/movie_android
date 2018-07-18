package com.bethejustice.myapplication4.CommentData;

import com.bethejustice.myapplication4.MovieData.Movie;

import java.util.ArrayList;

public class ResponseComment {

    public String message;
    public int code;
    public String resultType;
    public ArrayList<CommentItem> result  = new ArrayList<>();
}
