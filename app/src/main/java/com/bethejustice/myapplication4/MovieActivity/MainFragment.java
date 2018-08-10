package com.bethejustice.myapplication4.MovieActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bethejustice.myapplication4.AppHelper;
import com.bethejustice.myapplication4.CommentActivity.CommentAdapter;
import com.bethejustice.myapplication4.CommentActivity.CommentListActivity;
import com.bethejustice.myapplication4.CommentData.ResponseComment;
import com.bethejustice.myapplication4.MovieData.MovieInfo;
import com.bethejustice.myapplication4.R;
import com.bethejustice.myapplication4.CommentActivity.CommentWriteActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment {
    int like = 15;
    int dislike = 1;
    boolean thumb_up_s = false;
    boolean thumb_down_s = false;
    int movieId;
    RecyclerView recyclerView;
    RecyclerView imageRecyclerView;
    CommentAdapter adapter;
    ImageAdapter imageAdapter;

    Button thumbUpButton;
    Button thumbDownButton;
    TextView likeView;
    TextView dislikeView;
    RatingBar ratingBar;

    MovieInfo movieInfo;
    private InteractionListener listener;

    ResponseComment responseComment;

    public static MainFragment newInstance(MovieInfo movie) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putParcelable("movieInfo", movie);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (InteractionListener) context;
        } catch (ClassCastException castException) {

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 100) {

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        //toolbar title 수정
        listener.changeAppTitle("영화 상세");

        //parcelable data
        movieInfo = getArguments().getParcelable("movieInfo");

        //image view
        ImageView thumbView = (ImageView) rootView.findViewById(R.id.img_thumb);

        //button view
        thumbUpButton = rootView.findViewById(R.id.thumbUpButton);
        thumbDownButton = rootView.findViewById(R.id.thumbDownButton);
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        Button seeAllButton = (Button) rootView.findViewById(R.id.seeAllButton);
        Button commentWriteButton = (Button) rootView.findViewById(R.id.commentWriteButton);

        //text view
        TextView titleView = rootView.findViewById(R.id.text_title);
        TextView dateView = rootView.findViewById(R.id.text_date);
        TextView genreView = rootView.findViewById(R.id.text_genre_duration);
        likeView = (TextView) rootView.findViewById(R.id.text_like);
        dislikeView = (TextView) rootView.findViewById(R.id.text_dislike);
        TextView reservationRateView = rootView.findViewById(R.id.text_reservation_grade_rate);
        TextView userRatingView = rootView.findViewById(R.id.text_user_rating);
        TextView audienceView = rootView.findViewById(R.id.text_audience);
        TextView synopsisView = rootView.findViewById(R.id.text_synopsis);
        TextView directorView = rootView.findViewById(R.id.text_director);
        TextView actorView = rootView.findViewById(R.id.text_actor);

        if (movieInfo != null) {
            like = movieInfo.like;
            dislike = movieInfo.dislike;
            ratingBar.setRating(movieInfo.user_rating);
            likeView.setText(like + "");
            dislikeView.setText(dislike + "");
            movieId = movieInfo.getId();

            Glide.with(container).load(movieInfo.getThumb()).into(thumbView);
            titleView.setText(movieInfo.getTitle());
            dateView.setText(movieInfo.getDate() + " 개봉");
            genreView.setText(movieInfo.getGenre() + " / " + movieInfo.getDuration() + "분");
            reservationRateView.setText(String.format(movieInfo.getReservation_grade() + "위 " + movieInfo.getReservation_rate()) + "%");
            userRatingView.setText(movieInfo.getUser_rating() + "");
            audienceView.setText(movieInfo.getAudience() + "");
            synopsisView.setText(movieInfo.getSynopsis());
            directorView.setText(movieInfo.getDirector());
            actorView.setText(movieInfo.getActor());
        }

        //comment recyclerView
        if (AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(container.getContext());
            Log.d("main", "requestQueue");
        }

        recyclerView = rootView.findViewById(R.id.View_commentList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommentAdapter(container.getContext());
        recyclerView.setAdapter(adapter);
        sendRequest();

        //gallery recyclerView

        if (movieInfo.getPhotos() != null && movieInfo.getVideos() != null) {
            imageRecyclerView = rootView.findViewById(R.id.view_gallery);
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(container.getContext(), LinearLayoutManager.HORIZONTAL, false);
            imageRecyclerView.setLayoutManager(layoutManager1);
            imageAdapter = new ImageAdapter(container.getContext());
            imageRecyclerView.setAdapter(imageAdapter);

            ArrayList<GalleryItem> imageList = stringToGalleryItem(movieInfo.photos, 0);
            ArrayList<GalleryItem> videoList = stringToGalleryItem(movieInfo.videos,1);

            imageAdapter.addItemAll(imageList);
            imageAdapter.addItemAll(videoList);
            imageAdapter.notifyDataSetChanged();

            imageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ImageAdapter.ViewHolder holder, View view, int position) {
                    GalleryItem item = imageAdapter.getItem(position);
                    Intent intent = new Intent(getContext(), ImgActivity.class);
                    intent.putExtra("url", item.getUrl());

                    if(item.getDistinct()==1) {
                        intent = new Intent(Intent.ACTION_VIEW)
                                .setData(Uri.parse(item.getUrl()))
                                .setPackage("com.google.android.youtube");
                    }
                    startActivityForResult(intent, 1);
                }
            });
        }

        thumbDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!thumb_down_s) {
                    if (thumb_up_s) {
                        like--;
                        thumb_up_s = false;
                        thumbUpButton.setBackgroundResource(R.drawable.ic_thumb_up);
                    }
                    dislike++;
                    thumbDownButton.setBackgroundResource(R.drawable.ic_thumb_down_selected);
                } else {
                    dislike--;
                    thumbDownButton.setBackgroundResource(R.drawable.ic_thumb_down);
                }
                thumb_down_s = !thumb_down_s;
                likeView.setText(Integer.toString(like));
                dislikeView.setText(Integer.toString(dislike));
            }
        });

        thumbUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!thumb_up_s) {
                    if (thumb_down_s) {
                        dislike--;
                        thumbDownButton.setBackgroundResource(R.drawable.ic_thumb_down);
                        thumb_down_s = false;
                    }
                    like++;
                    thumbUpButton.setBackgroundResource(R.drawable.ic_thumb_up_selected);
                } else {
                    like--;
                    thumbUpButton.setBackgroundResource(R.drawable.ic_thumb_up);
                }
                thumb_up_s = !thumb_up_s;
                likeView.setText(Integer.toString(like));
                dislikeView.setText(Integer.toString(dislike));
            }
        });

        seeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CommentListActivity.class);
                intent.putExtra("rating", movieInfo.getUser_rating())
                        .putExtra("title", movieInfo.getTitle())
                        .putExtra("movieId", movieId);
                startActivity(intent);
            }
        });

        commentWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CommentWriteActivity.class);
                intent.putExtra("title", movieInfo.getTitle());
                startActivity(intent);
            }
        });
        return rootView;
    }

    public void sendRequest() {
        String url = "http://boostcourse-appapi.connect.or.kr:10000//movie/readCommentList?id=" + movieId + "&limit=2";
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

        setCommentList();
    }

    public void setCommentList() {
        adapter.addItemAll(responseComment);
        adapter.notifyDataSetChanged();
    }

    public ArrayList<GalleryItem> stringToGalleryItem(String photos, int distinct) {
        if (photos != null) {
            String[] string = photos.split(",");
            ArrayList<GalleryItem> photoList = new ArrayList<>();

            for(String url : string){
                photoList.add(new GalleryItem(distinct, url));
            }
            return photoList;
        }
        return null;
    }
}
