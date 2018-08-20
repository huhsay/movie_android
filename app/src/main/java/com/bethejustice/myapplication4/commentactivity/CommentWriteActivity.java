package com.bethejustice.myapplication4.commentactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bethejustice.myapplication4.AppHelper;
import com.bethejustice.myapplication4.R;

public class CommentWriteActivity extends AppCompatActivity {

    RatingBar ratingBar;
    EditText commentView;
    int movieId;
    String title;
    float rating;
    Toolbar toolbar;
    String userId = "Theodore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_write);


        Intent receivedIntent = getIntent();
        movieId = receivedIntent.getIntExtra("movieId", 0);
        title = receivedIntent.getStringExtra("title");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.text_reviewWrite));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setElevation(0);

        ratingBar =(RatingBar) findViewById(R.id.ratingBar);
        commentView = (EditText) findViewById(R.id.review);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        TextView titleView = findViewById(R.id.title);
        titleView.setText(title);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = commentView.getText().toString();
                rating = ratingBar.getRating() * 2;
                sendRequest(comment, rating);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendRequest(String review, float rating) {
        String url = "http://boostcourse-appapi.connect.or.kr:10000//movie/createComment?id=%d&writer=%s&rating=%f&contents=%s";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                String.format(url, movieId, userId, rating, review),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("commentWriteRequest", response);
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
}
