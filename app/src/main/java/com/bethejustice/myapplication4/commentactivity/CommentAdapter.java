package com.bethejustice.myapplication4.commentactivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bethejustice.myapplication4.commentdata.Comment;
import com.bethejustice.myapplication4.commentdata.ResponseComment;
import com.bethejustice.myapplication4.R;
import com.bethejustice.myapplication4.movieactivity.ImageAdapter;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    Context context;
    ResponseComment itemList;
    ArrayList<Comment> items = new ArrayList<>();
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder, View view, int position);

    }

    public CommentAdapter(Context context) {
        this.context = context;
    }

    // manager가 호출하는 callback 메소드
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.view_comment_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment item = items.get(position);
        holder.setItem(item);
        holder.setOnItemClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView userId;
        TextView time;
        RatingBar ratingBar;
        TextView comment;
        TextView comment_like;
        TextView recomment;
        OnItemClickListener listener;

        public ViewHolder(View itemView){
            super(itemView);

            userId = itemView.findViewById(R.id.userId);
            time = itemView.findViewById(R.id.time);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            comment = itemView.findViewById(R.id.review);
            comment_like = itemView.findViewById(R.id.comment_like);
            recomment = itemView.findViewById(R.id.btn_recomment);

            recomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener!=null){
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(Comment item) {
            if(userId!=null) {
                userId.setText(item.getWriter());
                time.setText(item.getTime());
                ratingBar.setRating(item.getRating() / 2);
                comment.setText(item.getContents());
                comment_like.setText(item.getRecommend() + "");
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }
    }

    public Comment getItem(int position) {return items.get(position);}

    public void addItem(Comment item) { items.add(item);}

    public void addItemAll(ArrayList<Comment> itemsList) {
        //this.itemList = itemsList;
        this.items = itemsList;}

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
