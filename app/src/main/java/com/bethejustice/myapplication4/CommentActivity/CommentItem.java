package com.bethejustice.myapplication4.CommentActivity;

public class CommentItem {
    String userId;
    String time;
    String comment;
    int like_cnt=0;

    public CommentItem(String userId, String time, String comment, int like_cnt) {
        this.userId = userId;
        this.time = time;
        this.comment = comment;
        this.like_cnt = like_cnt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getLike_cnt() {
        return like_cnt;
    }

    public void setLike_cnt(int like_cnt) {
        this.like_cnt = like_cnt;
    }
}
