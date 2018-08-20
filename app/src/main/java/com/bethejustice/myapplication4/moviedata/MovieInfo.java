package com.bethejustice.myapplication4.moviedata;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class MovieInfo implements Parcelable{

    private String title;
    private int id;
    private String date;
    private float user_rating;
    private float audience_rating;
    private float reviewer_rating;
    private float reservation_rate;
    private int reservation_grade;
    private int grade;
    private String thumb;
    private String image;
    private String photos;
    private String videos;
    private String outlinks;
    private String genre;
    private int duration;
    private int audience;
    private String synopsis;
    private String director;
    private String actor;
    private int like;
    private int dislike;


    protected MovieInfo(Parcel in) {
        title = in.readString();
        id = in.readInt();
        date = in.readString();
        user_rating = in.readFloat();
        audience_rating = in.readFloat();
        reviewer_rating = in.readFloat();
        reservation_rate = in.readFloat();
        reservation_grade = in.readInt();
        grade = in.readInt();
        thumb = in.readString();
        image = in.readString();
        photos = in.readString();
        videos = in.readString();
        outlinks = in.readString();
        genre = in.readString();
        duration = in.readInt();
        audience = in.readInt();
        synopsis = in.readString();
        director = in.readString();
        actor = in.readString();
        like = in.readInt();
        dislike = in.readInt();
    }

    public MovieInfo(Cursor in) {
        title = in.getString(0);
        id = in.getInt(1);
        date = in.getString(2);
        user_rating = in.getFloat(3);
        audience_rating = in.getFloat(4);
        reviewer_rating = in.getFloat(5);
        reservation_rate = in.getFloat(6);
        reservation_grade = in.getInt(7);
        grade = in.getInt(8);
        thumb = in.getString(9);
        image = in.getString(10);
        photos = in.getString(11);
        videos = in.getString(12);
        outlinks = in.getString(13);
        genre = in.getString(14);
        duration = in.getInt(15);
        audience = in.getInt(16);
        synopsis = in.getString(17);
        director = in.getString(18);
        actor = in.getString(19);
        like = in.getInt(20);
        dislike = in.getInt(21);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(id);
        dest.writeString(date);
        dest.writeFloat(user_rating);
        dest.writeFloat(audience_rating);
        dest.writeFloat(reviewer_rating);
        dest.writeFloat(reservation_rate);
        dest.writeInt(reservation_grade);
        dest.writeInt(grade);
        dest.writeString(thumb);
        dest.writeString(image);
        dest.writeString(photos);
        dest.writeString(videos);
        dest.writeString(outlinks);
        dest.writeString(genre);
        dest.writeInt(duration);
        dest.writeInt(audience);
        dest.writeString(synopsis);
        dest.writeString(director);
        dest.writeString(actor);
        dest.writeInt(like);
        dest.writeInt(dislike);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(float user_rating) {
        this.user_rating = user_rating;
    }

    public float getAudience_rating() {
        return audience_rating;
    }

    public void setAudience_rating(float audience_rating) {
        this.audience_rating = audience_rating;
    }

    public float getReviewer_rating() {
        return reviewer_rating;
    }

    public void setReviewer_rating(float reviewer_rating) {
        this.reviewer_rating = reviewer_rating;
    }

    public float getReservation_rate() {
        return reservation_rate;
    }

    public void setReservation_rate(float reservation_rate) {
        this.reservation_rate = reservation_rate;
    }

    public int getReservation_grade() {
        return reservation_grade;
    }

    public void setReservation_grade(int reservation_grade) {
        this.reservation_grade = reservation_grade;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    public String getOutlinks() {
        return outlinks;
    }

    public void setOutlinks(String outlinks) {
        this.outlinks = outlinks;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAudience() {
        return audience;
    }

    public void setAudience(int audience) {
        this.audience = audience;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public static Creator<MovieInfo> getCREATOR() {
        return CREATOR;
    }
}
