package com.bethejustice.myapplication4.MovieActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bethejustice.myapplication4.MovieData.Movie;
import com.bethejustice.myapplication4.R;
import com.bumptech.glide.Glide;

/**
 *  To do list
 *  data format ( date, float, etc)
 *
 */

public class MovieListFragment extends Fragment {
    static MainActivity activity;
    int userId;

    public static MovieListFragment newInstance(Movie movie){
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putParcelable("movie", movie);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        activity=(MainActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.list_fragment, container, false);

        ImageView poster = rootView.findViewById(R.id.image_thumb);
        TextView title = (TextView) rootView.findViewById(R.id.text_title);
        TextView reservationRate = (TextView) rootView.findViewById(R.id.text_reservation_rate);
        Button button = rootView.findViewById(R.id.btn_movie);

        Movie temp = getArguments().getParcelable("movie");

        Glide.with(container).load(temp.getImage()).into(poster);
        userId = temp.getId();
        title.setText(userId+ ". " +temp.getTitle());
        reservationRate.setText("예매율 : "+ String.format("%f",temp.getReservation_rate())+" | "+String.format("%d", temp.getGrade())+"세 관람가 | 날짜");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.sendRequest(userId);
            }
        });
        Log.d("movieIdAtList",userId+"");

        return rootView;
    }
}
