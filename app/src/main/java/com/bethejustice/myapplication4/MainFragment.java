package com.bethejustice.myapplication4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment{

    int like_cnt=15;
    int dislike_cnt=1;
    boolean thumb_up_s=false;
    boolean thumb_down_s=false;
    float rating;

    Button thumbUpButton;
    Button thumbDownButton;
    TextView like;
    TextView dislike;
    RatingBar ratingBar;

    MainActivity mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = new MainActivity();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == 101){
            // requestCode 101 commentWriteButton
            if(intent !=null){
                if(resultCode==RESULT_OK){
                    String comment = intent.getStringExtra("comment");
                    Toast.makeText(getContext(), comment, Toast.LENGTH_LONG).show();
                }
            }

        }else if(requestCode == 102){
            //requestCode 102 seeallButton;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);



        thumbUpButton = (Button) rootView.findViewById(R.id.thumbUpButton);
        thumbDownButton = (Button) rootView.findViewById(R.id.thumbDownButton);
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        Button seeAllButton = (Button) rootView.findViewById(R.id.seeAllButton);
        Button commentWriteButton = (Button) rootView.findViewById(R.id.commentWriteButton);
        CommentItemAdapter adapter = new CommentItemAdapter();
        ListView list = (ListView) rootView.findViewById(R.id.commentList);
        list.setAdapter(adapter);

        adapter.addItem(new CommentItem("id", "hi","hi", 1));
        adapter.addItem(new CommentItem("id", "hi","hi", 1));
        adapter.addItem(new CommentItem("id", "hi","hi", 1));
        adapter.notifyDataSetChanged();

        rating = ratingBar.getRating();
        like = (TextView) rootView.findViewById(R.id.like);
        dislike =(TextView) rootView.findViewById(R.id.dislike);
        like.setText(Integer.toString(like_cnt));
        dislike.setText(Integer.toString(dislike_cnt));

        thumbDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!thumb_down_s){

                    if(thumb_up_s){
                        like_cnt--;
                        thumb_up_s = false;
                        thumbUpButton.setBackgroundResource(R.drawable.ic_thumb_up);
                        dislike_cnt++;
                        thumb_down_s = true;
                        thumbDownButton.setBackgroundResource(R.drawable.ic_thumb_down_selected);

                    }else{
                        dislike_cnt++;
                        thumb_down_s = true;
                        thumbDownButton.setBackgroundResource(R.drawable.ic_thumb_down_selected);


                    }

                }else{

                    thumb_down_s = false;
                    dislike_cnt--;
                    thumbDownButton.setBackgroundResource(R.drawable.ic_thumb_down);

                }

                like.setText(Integer.toString(like_cnt));
                dislike.setText(Integer.toString(dislike_cnt));
            }
        });

        thumbUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!thumb_up_s){

                    if(thumb_down_s){
                        dislike_cnt--;
                        thumbDownButton.setBackgroundResource(R.drawable.ic_thumb_down);
                        thumb_down_s = false;
                        like_cnt++;
                        thumb_up_s = true;
                        thumbUpButton.setBackgroundResource(R.drawable.ic_thumb_up_selected);
                    }else{
                        like_cnt++;
                        thumb_up_s = true;
                        thumbUpButton.setBackgroundResource(R.drawable.ic_thumb_up_selected);
                    }

                }else{

                    thumb_up_s = false;
                    like_cnt--;
                    thumbUpButton.setBackgroundResource(R.drawable.ic_thumb_up);

                }

                like.setText(Integer.toString(like_cnt));
                dislike.setText(Integer.toString(dislike_cnt));
            }
        });



        seeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"seeAllButton was cliked", Toast.LENGTH_LONG).show();

                Log.d("intent","fragment");
                Intent intent = new Intent(getContext(), CommentListActivity.class);
                startActivity(intent);

            }
        });

        commentWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"CommentWriteButton was cliked"+rating,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), writeComment.class);
                intent.putExtra("rating", rating);
                startActivity(intent);
            }
        });

        return rootView;
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
                view = new CommentItemView(getContext());
            }else{
                view = (CommentItemView)convertView;
            }

            CommentItem item = commentItems.get(position);


            return view;
        }
    }
}
