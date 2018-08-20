package com.bethejustice.myapplication4.commentdata;

import android.database.Cursor;

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

    public Comment(Cursor in) {
        this.id = in.getInt(0);
        this.writer = in.getString(1);
        this.movieId = in.getInt(2);
        this.writer_image = in.getString(3);
        this.time = in.getString(4);
        this.timestamp = in.getInt(5);
        this.rating = in.getFloat(6);
        this.contents = in.getString(7);
        this.recommend = in.getInt(8);
    }

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
