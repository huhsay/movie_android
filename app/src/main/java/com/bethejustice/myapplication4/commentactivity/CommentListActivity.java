package com.bethejustice.myapplication4.commentactivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bethejustice.myapplication4.AppHelper;
import com.bethejustice.myapplication4.NetworkState;
import com.bethejustice.myapplication4.R;
import com.bethejustice.myapplication4.commentdata.Comment;
import com.bethejustice.myapplication4.commentdata.ResponseComment;
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

    NetworkState networkState;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == 500) {
                if (networkState.checkNetworkConnection() == NetworkState.TYPE_NOT_CONNECTED) {
                    Cursor commentCursor = DatabaseHelper.selectComment(movieId);
                    ArrayList<Comment> list = new ArrayList<>();
                    while (commentCursor.moveToNext()) {
                        Comment temp = new Comment(commentCursor);
                        list.add(temp);
                    }
                    setCommentList(list);
                } else {

                    sendRequest();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.text_reviewList));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent receivedIntent = getIntent();
        titleString = receivedIntent.getStringExtra("title");
        user_rating = receivedIntent.getFloatExtra("rating",0);
        movieId = receivedIntent.getIntExtra("movieId",0);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating(receivedIntent.getFloatExtra("rating", 0f));
        TextView ratings = findViewById(R.id.ratings);
        ratings.setText(String.format("%f (1,142명 참여)", user_rating));
        final TextView title = findViewById(R.id.title);
        title.setText(titleString);


        recyclerView = findViewById(R.id.view_commentList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommentAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CommentAdapter.OnItemClickListener() {
                                           @Override
                                           public void onItemClick(CommentAdapter.ViewHolder holder, View view, int position) {
                                               Comment item = adapter.getItem(position);
                                               sendCommentLikeRequest(item.getId(), item.getWriter());
                                               Toast.makeText(getApplicationContext(), String.format("%s님의 한줄평을 추천하였습니다.", item.getWriter()), Toast.LENGTH_LONG).show();
                                           }
                                       });

        networkState = new NetworkState(getApplicationContext());

        if(networkState.checkNetworkConnection() == NetworkState.TYPE_NOT_CONNECTED) {
            Cursor commentCursor = DatabaseHelper.selectComment(movieId);
            ArrayList<Comment> list = new ArrayList<>();
            while(commentCursor.moveToNext()){
                Comment temp = new Comment(commentCursor);
                list.add(temp);
            }
            sendRequest();
            //setCommentList(list);
        }else{

            sendRequest();
        }

        TextView writeButton = findViewById(R.id.commentWriteButton);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CommentWriteActivity.class);
                intent.putExtra("title", titleString)
                        .putExtra("movieId", movieId);
                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 500);
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


    public void sendRequest() {
        String url = "http://boostcourse-appapi.connect.or.kr:10000//movie/readCommentList?id=" + movieId;

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
        //DatabaseHelper.insertComment(responseComment.result);
        setCommentList(responseComment.result);
    }

    public void sendCommentLikeRequest(int reviewId, String writer) {
        String url = "http://boostcourse-appapi.connect.or.kr:10000//movie/increaseRecommend?review_id=" + reviewId +"&writer=" + writer+"&limit=100";


        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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

    public void setCommentList(ArrayList<Comment> list) {
        adapter.addItemAll(list);
        adapter.notifyDataSetChanged();
    }
}
