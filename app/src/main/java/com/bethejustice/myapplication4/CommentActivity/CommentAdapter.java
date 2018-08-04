package com.bethejustice.myapplication4.CommentActivity;

import android.content.Context;
import android.media.Rating;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bethejustice.myapplication4.CommentData.Comment;
import com.bethejustice.myapplication4.CommentData.ResponseComment;
import com.bethejustice.myapplication4.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.bethejustice.myapplication4.R.id.userId;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    Context context;
    ResponseComment itemList;
    ArrayList<Comment> items = new ArrayList<>();

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

        public ViewHolder(View itemView){
            super(itemView);

            userId = (TextView) itemView.findViewById(R.id.userId);
            time = (TextView) itemView.findViewById(R.id.time);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            comment = (TextView) itemView.findViewById(R.id.comment);
            comment_like = (TextView) itemView.findViewById(R.id.comment_like);

        }

        public void setItem(Comment item) {
            if(userId!=null){
            userId.setText(item.getWriter());}
            time.setText(item.getTime());
            ratingBar.setRating(item.getRating());
            comment.setText(item.getContents());
            comment_like.setText(item.getRecommend()+"");
        }


    }

    public void addItem(Comment item) { items.add(item);}

    public void addItemAll(ResponseComment itemsList) {
        this.itemList = itemsList;
        this.items = itemsList.getResult();}



}
