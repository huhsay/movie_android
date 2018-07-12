package com.bethejustice.myapplication4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class CommentListActivity extends AppCompatActivity {
    CommentItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        ListView listView = (ListView) findViewById(R.id.commentList);
        adapter = new CommentItemAdapter();
        listView.setAdapter(adapter);

        adapter.addItem(new CommentItem("hi","hi","hi",1));
        adapter.addItem(new CommentItem("hi","hi","hi",1));
        adapter.addItem(new CommentItem("hi","hi","hi",1));
        adapter.addItem(new CommentItem("hi","hi","hi",1));
        adapter.addItem(new CommentItem("hi","hi","hi",1));
        adapter.addItem(new CommentItem("hi","hi","hi",1));
        adapter.addItem(new CommentItem("hi","hi","hi",1));
        adapter.addItem(new CommentItem("hi","hi","hi",1));
        adapter.addItem(new CommentItem("hi","hi","hi",1));
        adapter.notifyDataSetChanged();

        Button writeButton = (Button) findViewById(R.id.commentWriteButton);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), writeComment.class);
                startActivity(intent);
            }
        });
    }


    class CommentItemAdapter extends BaseAdapter {
        ArrayList<CommentItem> commentItems = new ArrayList<>();

        public void addItem(CommentItem item){
            commentItems.add(item);
        }

        @Override
        public int getCount() {
            return commentItems.size();
        }

        @Override
        public Object getItem(int position) {
            return commentItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            CommentItemView view= null;
            if(convertView == null){
                view = new CommentItemView(getApplicationContext());
            }else{
                view = (CommentItemView)convertView;
            }

            CommentItem item = commentItems.get(position);


            return view;
        }
    }
}
