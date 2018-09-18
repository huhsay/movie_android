package com.bethejustice.myapplication4.movieactivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.bethejustice.myapplication4.commentactivity.CommentAdapter;
import com.bethejustice.myapplication4.commentactivity.CommentListActivity;
import com.bethejustice.myapplication4.commentdata.Comment;
import com.bethejustice.myapplication4.commentdata.ResponseComment;
import com.bethejustice.myapplication4.moviedata.MovieInfo;
import com.bethejustice.myapplication4.NetworkState;
import com.bethejustice.myapplication4.R;
import com.bethejustice.myapplication4.commentactivity.CommentWriteActivity;
import com.bethejustice.myapplication4.database.DatabaseHelper;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    final int LIKE = 1;
    final int DISLIKE = 2;
    final int NEUTRALITY = 0;

    int likeCunt = 15;
    int dislikeCunt = 1;
    int preference = NEUTRALITY;

    int movieId;
    RecyclerView recyclerView;
    RecyclerView imageRecyclerView;
    ImageAdapter imageAdapter;

    Button thumbUpButton;
    Button thumbDownButton;
    TextView likeView;
    TextView dislikeView;
    RatingBar ratingBar;

    MovieInfo movieInfo;
    InteractionListener listener;
    NetworkState networkState;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == -1){
            if(requestCode == 400){
                Toast.makeText(getContext(), "result fragment", Toast.LENGTH_LONG);
            }
        }
    }

    CommentAdapter adapter;
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
            castException.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        //toolbar title 수정
        listener.changeAppTitle("영화 상세");
        listener.removeOrderMenus(true);

        //parcelable data
        movieInfo = getArguments().getParcelable("movieInfo");

        //image view
        ImageView thumbView = rootView.findViewById(R.id.img_thumb);

        //button view
        thumbUpButton = rootView.findViewById(R.id.thumbUpButton);
        thumbDownButton = rootView.findViewById(R.id.thumbDownButton);
        ratingBar = rootView.findViewById(R.id.ratingBar);
        Button seeAllButton = rootView.findViewById(R.id.seeAllButton);
        Button commentWriteButton = rootView.findViewById(R.id.commentWriteButton);

        //text view
        TextView titleView = rootView.findViewById(R.id.text_title);
        TextView dateView = rootView.findViewById(R.id.text_date);
        TextView genreView = rootView.findViewById(R.id.text_genre_duration);
        TextView reservationRateView = rootView.findViewById(R.id.text_reservation_grade_rate);
        TextView userRatingView = rootView.findViewById(R.id.text_user_rating);
        TextView audienceView = rootView.findViewById(R.id.text_audience);
        TextView synopsisView = rootView.findViewById(R.id.text_synopsis);
        TextView directorView = rootView.findViewById(R.id.text_director);
        TextView actorView = rootView.findViewById(R.id.text_actor);
        likeView = rootView.findViewById(R.id.text_like);
        dislikeView = rootView.findViewById(R.id.text_dislike);

        if (movieInfo != null) {
            likeCunt = movieInfo.getLike();
            dislikeCunt = movieInfo.getDislike();
            ratingBar.setRating(movieInfo.getUser_rating());
            Log.d("ratingbar", Float.toString(movieInfo.getUser_rating()));
            likeView.setText(Integer.toString(likeCunt));
            dislikeView.setText(Integer.toString(dislikeCunt));
            movieId = movieInfo.getId();

            Glide.with(container).load(movieInfo.getThumb()).into(thumbView);
            titleView.setText(movieInfo.getTitle());
            dateView.setText(String.format("%s 개봉", movieInfo.getDate()));
            genreView.setText(String.format("%s / %d 분" , movieInfo.getGenre(), movieInfo.getDuration()));
            reservationRateView.setText(String.format(movieInfo.getReservation_grade() + "위 " + movieInfo.getReservation_rate()) + "%");
            userRatingView.setText(Float.toString(movieInfo.getUser_rating()));
            audienceView.setText(String.format("%,d", movieInfo.getAudience()));
            synopsisView.setText(movieInfo.getSynopsis());
            directorView.setText(movieInfo.getDirector());
            actorView.setText(movieInfo.getActor());
        }

        //comment recyclerView
        if (AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(container.getContext());
        }

        recyclerView = rootView.findViewById(R.id.View_commentList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommentAdapter(container.getContext());
        recyclerView.setAdapter(adapter);

        // 데이터베이스에서 가져와서 2개의 코맨트 입력하기
        networkState = new NetworkState(container.getContext());
        if (networkState.checkNetworkConnection() == NetworkState.TYPE_NOT_CONNECTED) {
            readCommentDate();
        }else{
            sendRequest();
        }

        //gallery recyclerView
        if (movieInfo.getPhotos() != null && movieInfo.getVideos() != null) {
            imageRecyclerView = rootView.findViewById(R.id.view_gallery);
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(container.getContext(), LinearLayoutManager.HORIZONTAL, false);
            imageRecyclerView.setLayoutManager(layoutManager1);
            imageAdapter = new ImageAdapter(container.getContext());
            imageRecyclerView.setAdapter(imageAdapter);

            ArrayList<GalleryItem> imageList = stringToGalleryItem(movieInfo.getPhotos(), 0);
            ArrayList<GalleryItem> videoList = stringToGalleryItem(movieInfo.getVideos(), 1);

            imageAdapter.addItemAll(imageList);
            imageAdapter.addItemAll(videoList);
            imageAdapter.notifyDataSetChanged();

            imageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ImageAdapter.ViewHolder holder, View view, int position) {
                    GalleryItem item = imageAdapter.getItem(position);
                    Intent intent = new Intent(getContext(), ImgActivity.class);
                    if(item.getType() == GalleryItem.IMAGE){
                        intent.putExtra("url", item.getUrl());
                    }

                    if (item.getType() == GalleryItem.VIDEO) {
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
                switch (preference){
                    case NEUTRALITY:
                        increaseDislikeCount();
                        preference = DISLIKE;
                        break;
                    case DISLIKE:
                        decreaseDislikeCount();
                        preference = NEUTRALITY;
                        break;
                    case LIKE:
                        increaseDislikeCount();
                        decreaseLikeCount();
                        preference = DISLIKE;
                        break;
                }
            }

        });

        thumbUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (preference){
                    case NEUTRALITY:
                        increaseLikeCount();
                        preference = LIKE;
                        break;
                    case DISLIKE:
                        decreaseDislikeCount();
                        increaseLikeCount();
                        preference = LIKE;
                        break;
                    case LIKE:
                        decreaseLikeCount();
                        preference = NEUTRALITY;
                        break;
                }
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
                intent.putExtra("title", movieInfo.getTitle())
                        .putExtra("movieId", movieId);
                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 400);
            }
        });
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

    private void processResponse(String response) {
        Gson gson = new Gson();
        responseComment = gson.fromJson(response, ResponseComment.class);

        setCommentList();
    }

    private void setCommentList() {
        adapter.addItemAll(responseComment.result);
        adapter.notifyDataSetChanged();
    }

    public void readCommentDate() {

        Cursor commentCursor = DatabaseHelper.selectComment(movieId);
        ArrayList<Comment> list = new ArrayList<>();

        for(int i = 0 ; i < 2; i++) {
            if(commentCursor.moveToNext()) {
                Comment temp = new Comment(commentCursor);
                list.add(temp);
            }
        }
//        if (commentCursor.moveToPrevious()) {
//            Comment temp = new Comment(commentCursor);
//            list.add(temp);
//            adapter.addItemAll(list);
//            adapter.notifyDataSetChanged();
//        }

        adapter.addItemAll(list);
        adapter.notifyDataSetChanged();
    }

    public ArrayList<GalleryItem> stringToGalleryItem(String photos, int distinct) {
        if (photos != null) {
            String[] string = photos.split(",");
            ArrayList<GalleryItem> GalleryItemList = new ArrayList<>();

            for (String url : string) {
                GalleryItemList.add(new GalleryItem(distinct, url));
            }
            return GalleryItemList;
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.changeAppTitle(getResources().getString(R.string.text_movie));
        listener.removeOrderMenus(true);

        if (networkState.checkNetworkConnection() == NetworkState.TYPE_NOT_CONNECTED) {
            readCommentDate();
        }else{
            sendRequest();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

        listener.changeAppTitle(getResources().getString(R.string.text_movieList));
        listener.removeOrderMenus(false);
    }

    public void sendLikeRequest(Object like, Object dislike) {
        String likeUrl = "http://boostcourse-appapi.connect.or.kr:10000//movie/increaseLikeDisLike?id=%d&likeyn=%c";
        String dislikeUrl = "http://boostcourse-appapi.connect.or.kr:10000//movie/increaseLikeDisLike?id=%d&dislikeyn=%c";
        String url = null;

        if(dislike==null){
            url = String.format(likeUrl, movieId, like);
        }
        if(like == null){
            url = String.format(dislikeUrl, movieId, dislike);
        }
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("comment","success");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("comment","unsuccess");
                    }
                }
        );

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    // 좋아요 버튼 관련 메소드
    public void increaseLikeCount(){
        likeCunt++;
        thumbUpButton.setBackgroundResource(R.drawable.ic_thumb_up_selected);
        likeView.setText(Integer.toString(likeCunt));
        sendLikeRequest('Y', null);
    }

    public void decreaseLikeCount(){
        likeCunt--;
        thumbUpButton.setBackgroundResource(R.drawable.ic_thumb_up);
        likeView.setText(Integer.toString(likeCunt));
        sendLikeRequest('N', null);
    }

    public void increaseDislikeCount(){
        dislikeCunt++;
        thumbDownButton.setBackgroundResource(R.drawable.ic_thumb_down_selected);
        dislikeView.setText(Integer.toString(dislikeCunt));
        sendLikeRequest(null, 'Y');
    }

    public void decreaseDislikeCount(){
        dislikeCunt--;
        thumbDownButton.setBackgroundResource(R.drawable.ic_thumb_down);
        dislikeView.setText(Integer.toString(dislikeCunt));
        sendLikeRequest(null, 'N');
    }
}
