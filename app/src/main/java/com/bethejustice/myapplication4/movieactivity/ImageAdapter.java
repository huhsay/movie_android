package com.bethejustice.myapplication4.movieactivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bethejustice.myapplication4.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{
    Context context;
    ArrayList<GalleryItem> items = new ArrayList<>();
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder, View view, int position);

    }

    public ImageAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.view_image_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GalleryItem item = items.get(position);
        holder.setItem(item);
        holder.setOnItemClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ImageView playIcon;
        OnItemClickListener listener;

        public ViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.view_image);
            playIcon = itemView.findViewById(R.id.icon_play);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(GalleryItem item) {
            try {
                String url = item.getUrl();
                if(item.getType() == GalleryItem.VIDEO){
                    playIcon.setVisibility(View.VISIBLE);
                    url = changeVideoUrl(url);
                }

                RequestOptions options = new RequestOptions();
                options.centerCrop();

                Glide.with(itemView)
                        .load(url)
                        .apply(options)
                        .into(imageView);

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

    }

    public void addItem(GalleryItem item) { items.add(item);}

    public void addItemAll(ArrayList<GalleryItem> items) {
        this.items.addAll(items);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public GalleryItem getItem(int position){
        return items.get(position);
    }

    private static String changeVideoUrl(String string){
        String[] temp = string.split("/");
        String url = "https://img.youtube.com/vi/"+temp[temp.length-1]+"/0.jpg";
        return url;
    }
}
