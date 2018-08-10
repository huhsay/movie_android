package com.bethejustice.myapplication4.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bethejustice.myapplication4.CommentData.Comment;
import com.bethejustice.myapplication4.MovieData.Movie;
import com.bethejustice.myapplication4.MovieData.MovieInfo;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static SQLiteDatabase database;
    private static String createTableOutlineSql = "create table if not exists outline" +
            "("+
            "    _id integer PRIMARY KEY autoincrement, " +
            "    id integer, " +
            "    title text, " +
            "    title_eng text, " +
            "    date text, " +
            "    user_rating float, " +
            "    audience_rating float, "+
            "    reviewer_rating float, "+
            "    reservation_rate float, "+
            "    reservation_grade integer, "+
            "    grade integer, "+
            "    thumb text, "+
            "    image text "+
            ")";

    private static String createTableMovieSql = "create table if not exists movie"+
            "(" +
            "    _id integer PRIMARY KEY autoincrement,"+
            "    title text, " +
            "    id integer," +
            "    date text," +
            "    user_rating float," +
            "    audience_rating float," +
            "    reviewer_rating float," +
            "    reservation_rate float," +
            "    reservation_grate integer," +
            "    grade integer," +
            "    thumb text," +
            "    image text," +
            "    photos text," +
            "    videos text," +
            "    outlinkes text," +
            "    genre text," +
            "    duration integer," +
            "    audience integer," +
            "    synopsis text," +
            "    director text," +
            "    actor text," +
            "    'like' integer," +
            "    dislike integer" +
            ")";

    private static String createTableReviewSql = "create table if not exists review" +
            "(" +
            "    _id integer PRIMARY KEY autoincrement," +
            "    id integer," +
            "    writer text," +
            "    movieId integer," +
            "    writer_image text," +
            "    time text," +
            "    timestamp integer," +
            "    rating float," +
            "    contents text," +
            "    recommend integer" +
            ")";


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public static void openDatabase(Context context, String databaseName){

        try {
            database = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
            Log.d("database", "open");
            createTable(database);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTable(SQLiteDatabase db) {
        if(db != null){
            db.execSQL(createTableOutlineSql);
            db.execSQL(createTableMovieSql);
            db.execSQL(createTableReviewSql);
        }
    }

    public static void insertMovieList(ArrayList<Movie> movieList){

        String insertMovieListSql = "INSERT INTO outline(id, title, title_eng, date, user_rating, audience_rating, reviewer_rating, reservation_rate, reservation_grade, grade, thumb, image) values(?,?,?,?,?,?,?,?,?,?,?,?)";

        for(int i = 0; i<movieList.size(); i++) {
            Movie temp = movieList.get(i);
            Object[] item = {temp.getId(), temp.getTitle(), temp.getTitle_eng(), temp.getDate(), temp.getUser_rating(), temp.getAudience_rating(), temp.getReviewer_rating(), temp.getReservation_rate(), temp.getReservation_grade(), temp.getGrade(), temp.getThumb(), temp.getImage()};
            database.execSQL(insertMovieListSql, item);
        }
    }

    public static void insertMovie(ArrayList<MovieInfo> movieInfoList){

        String insertMovieSql = "INSERT INTO movie(title, id, date, user_rating, audience_rating, reviewer_rating, reservation_rate, reservation_grate, grade, thumb, image, photos, videos, outlinkes, genre, duration, audience, synopsis, director, actor, 'like', dislike) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        for(int i = 0; i < movieInfoList.size(); i++) {
            MovieInfo temp = movieInfoList.get(i);
            Object[] item = {temp.getTitle(), temp.getId(), temp.getDate(), temp.getUser_rating(), temp.getAudience_rating(), temp.getReviewer_rating(), temp.getReservation_rate(), temp.getReservation_grade(), temp.getThumb(),temp.getImage(), temp.getPhotos(), temp.getVideos(), temp.getOutlinks(), temp.getGenre(), temp.getDuration(), temp.getAudience(), temp.getSynopsis(), temp.getDirector(), temp.getActor(), temp.getLike(), temp.getDislike()};
            database.execSQL(insertMovieSql, item);
        }
    }

    public static void insertComment(ArrayList<Comment> comment){

        String insertCommentSql = "INSERT INTO review(id, writer, movieId, writer_image, time, timestamp, rating, contents, recommend) values(?,?,?,?,?,?,?,?,?)";

        for(int i = 0; i < comment.size(); i++) {
            Comment temp = comment.get(i);
            Object[] item = {temp.getId(), temp.getWriter(), temp.getWriter_image(), temp.getTime(), temp.getTimestamp(), temp.getRating(), temp.getContents(), temp.getRecommend()};
            database.execSQL(insertCommentSql, item);
        }

    }


}
