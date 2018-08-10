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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MovieListFragment extends Fragment {
    private InteractionListener listener;
    int userId;

    public static MovieListFragment newInstance(Movie movie){
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putParcelable("movie", movie);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        super.onAttach(context);
        try{
            listener = (InteractionListener) context;
        } catch (ClassCastException castException){

        }
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

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list, container, false);

        ImageView poster = rootView.findViewById(R.id.image_thumb);
        TextView title = (TextView) rootView.findViewById(R.id.text_title);
        TextView reservationRate = (TextView) rootView.findViewById(R.id.text_reservation_rate);
        Button button = rootView.findViewById(R.id.btn_movie);

        Movie temp = getArguments().getParcelable("movie");

        Glide.with(container).load(temp.getImage()).into(poster);
        userId = temp.getId();
        title.setText(userId+ ". " +temp.getTitle());

        String date = getDateString(temp.getDate());
        reservationRate.setText("예매율 : "+ String.format("%.2f",temp.getReservation_rate())+"%  |  "+String.format("%d", temp.getGrade())+"세 관람가  |  "+date);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.sendRequest(userId);
            }
        });

        return rootView;
    }

    public String getDateString(String date){
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        Date dday = null;
        try {
            dday = (Date) format2.parse(date);
            Date today = new Date();
            Calendar d_day = Calendar.getInstance();
            Calendar today1 = Calendar.getInstance();
            today1.set(today.getYear(), today.getMonth(), today.getDate());
            d_day.set(dday.getYear(), dday.getMonth(), dday.getDate());

            long l_dday = d_day.getTimeInMillis();
            long l_today = today1.getTimeInMillis();

            long i = l_dday-l_today;


            if(i>0){
                return "D-"+i;
            }else if(i == 0){
                return "D-day";
            }

        }catch(Exception e){

        }

        SimpleDateFormat format1 = new SimpleDateFormat("yy.MM.dd 개봉");
        //이미 개봉했으면 개봉일을 리턴한다다
        String s = format1.format(dday);
        return s;
    }
}
