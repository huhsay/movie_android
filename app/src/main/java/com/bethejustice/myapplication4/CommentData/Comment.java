package com.bethejustice.myapplication4.CommentData;

public class Comment {

    int id;
    String writer;
    int movieId;
    String writer_image;
    String time;
    int timestamp;
    float rating;
    String contents;
    int recommend;

    public int getId() {
        return id;
    }

    public String getWriter() {
        return writer;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getWriter_image() {
        return writer_image;
    }

    public String getTime() {
        return time;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public float getRating() {
        return rating;
    }

    public String getContents() {
        return contents;
    }

    public int getRecommend() {
        return recommend;
    }
}
