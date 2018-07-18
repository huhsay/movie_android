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

import com.bethejustice.myapplication4.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    Context context;
    ArrayList<CommentItem> items = new ArrayList<>();

    public CommentAdapter(Context context) {
        this.context = context;
    }


    // manager가 호출하는 callback 메소드
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.comment_item_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentItem item = items.get(position);
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
        TextView commnet_like;

        public ViewHolder(View itemView){
            super(itemView);

            userId = itemView.findViewById(R.id.userId);
            time = itemView.findViewById(R.id.time);
        }

        public void setItem(CommentItem item) {
            userId.setText(item.getUserId());
            time.setText(item.getTime());
        }


    }

    public void addItem(CommentItem item) { items.add(item);}

    public void addItemAll(ArrayList<CommentItem> items) { this.items=items;};



}
