package com.bethejustice.myapplication4.CommentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bethejustice.myapplication4.AppHelper;
import com.bethejustice.myapplication4.CommentData.ResponseComment;
import com.bethejustice.myapplication4.R;
import com.bethejustice.myapplication4.database.DatabaseHelper;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CommentListActivity extends AppCompatActivity {
    CommentAdapter adapter;
    RecyclerView recyclerView;
    ResponseComment responseComment;
    Toolbar toolbar;

    String titleString;
    float user_rating;
    int movieId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        Intent receivedIntent = getIntent();
        titleString = receivedIntent.getStringExtra("title");
        user_rating = receivedIntent.getFloatExtra("rating",0);
        movieId = receivedIntent.getIntExtra("movieId",0);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating(receivedIntent.getFloatExtra("rating", 0f));
        TextView ratings = findViewById(R.id.ratings);
        ratings.setText(user_rating+"(1,142명 참여)");
        final TextView title = findViewById(R.id.title);
        title.setText(titleString);


        recyclerView = findViewById(R.id.view_commentList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommentAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);
        sendRequest();

        TextView writeButton = findViewById(R.id.commentWriteButton);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CommentWriteActivity.class);
                intent.putExtra("title",titleString);
                startActivity(intent);
            }
        });
    }


    public void sendRequest() {
        String url = "http://boostcourse-appapi.connect.or.kr:10000//movie/readCommentList?id=" + movieId+"&limit=100";

        Log.d("MainFragment", url);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    public void processResponse(String response) {
        Gson gson = new Gson();
        responseComment = gson.fromJson(response, ResponseComment.class);
        DatabaseHelper.insertComment(responseComment.result);
        setCommentList();
    }

    public void setCommentList() {
        adapter.addItemAll(responseComment);
        adapter.notifyDataSetChanged();
    }
}
