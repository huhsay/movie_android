package com.bethejustice.myapplication4.moviedata;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{
    //changed
    private int id;
    private String title;
    private String title_eng;
    private String date;
    private float user_rating;
    private float audience_rating;
    private float reviewer_rating;
    private float reservation_rate;
    private int reservation_grade;
    private int grade;
    private String thumb;
    private String image;

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        title_eng = in.readString();
        date = in.readString();
        user_rating = in.readInt();
        audience_rating = in.readInt();
        reviewer_rating = in.readInt();
        reservation_rate = in.readInt();
        reservation_grade = in.readInt();
        grade = in.readInt();
        thumb = in.readString();
        image = in.readString();
    }

    public Movie(Cursor in) {
        id = in.getInt(0);
        title = in.getString(1);
        title_eng = in.getString(2);
        date = in.getString(3);
        user_rating = in.getFloat(4);
        audience_rating = in.getFloat(5);
        reviewer_rating = in.getFloat(6);
        reservation_rate = in.getFloat(7);
        reservation_grade = in.getInt(8);
        grade = in.getInt(9);
        thumb = in.getString(10);
        image = in.getString(11);
    }



    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle_eng() {
        return title_eng;
    }

    public String getDate() {
        return date;
    }

    public float getUser_rating() {
        return user_rating;
    }

    public float getAudience_rating() {
        return audience_rating;
    }

    public float getReviewer_rating() {
        return reviewer_rating;
    }

    public float getReservation_rate() {
        return reservation_rate;
    }

    public int getReservation_grade() {
        return reservation_grade;
    }

    public int getGrade() {
        return grade;
    }

    public String getThumb() {
        return thumb;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(title_eng);
        dest.writeString(date);
        dest.writeFloat(user_rating);
        dest.writeFloat(audience_rating);
        dest.writeFloat(reviewer_rating);
        dest.writeFloat(reservation_rate);
        dest.writeInt(reservation_grade);
        dest.writeInt(grade);
        dest.writeString(thumb);
        dest.writeString(image);
    }
}
