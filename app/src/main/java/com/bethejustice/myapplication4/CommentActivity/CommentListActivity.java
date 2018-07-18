package com.bethejustice.myapplication4.CommentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.bethejustice.myapplication4.R;

import java.util.ArrayList;

public class CommentListActivity extends AppCompatActivity {
    CommentAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        recyclerView = findViewById(R.id.view_commentList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommentAdapter(getApplicationContext());
        adapter.addItem(new CommentItem("hi", "hi", "hi",1));
        adapter.addItem(new CommentItem("hi", "hi", "hi",1));
        adapter.addItem(new CommentItem("hi", "hi", "hi",1));
        recyclerView.setAdapter(adapter);

        Button writeButton = (Button) findViewById(R.id.commentWriteButton);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CommentWriteActivity.class);
                startActivity(intent);
            }
        });
    }
}
