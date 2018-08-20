package com.bethejustice.myapplication4.movieactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bethejustice.myapplication4.moviedata.Movie;
import com.bethejustice.myapplication4.NetworkState;
import com.bethejustice.myapplication4.R;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MovieListFragment extends Fragment {
    private InteractionListener listener;
    private int movieId;
    private NetworkState networkState;
    private Movie movie;

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
        super.onAttach(context);
        try{
            listener = (InteractionListener) context;
        } catch (ClassCastException castException){
            castException.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //인터넷 연결 확인
        networkState = new NetworkState(container.getContext());
        int network = networkState.checkNetworkConnection();
        movie = getArguments().getParcelable("movie");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list, container, false);
        ImageView poster = rootView.findViewById(R.id.image_thumb);
        TextView title = rootView.findViewById(R.id.text_title);
        TextView reservationRate = rootView.findViewById(R.id.text_reservation_rate);
        Button button = rootView.findViewById(R.id.btn_movie);

        Glide.with(container).load(movie.getImage()).into(poster);
        movieId = movie.getId();
        title.setText(movieId + ". " +movie.getTitle());
        reservationRate.setText("예매율 : "+ String.format("%.2f",movie.getReservation_rate())+"%  |  "+String.format("%d", movie.getGrade())+"세 관람가  |  "+ getDdayString(movie.getDate()));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(networkState.checkNetworkConnection()== NetworkState.TYPE_NOT_CONNECTED) {
                    listener.changeFragment(movieId);
                }else{
                    listener.sendRequest(movieId);
                }
            }
        });

        return rootView;
    }

    /**
     *  캘린더 함수 수정하기
     */

    public String getDdayString(String date){
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        Date releaseDay = null;
        Date today = new Date();

        try {
            releaseDay = format2.parse(date);
         }catch(Exception e){
            e.printStackTrace();
        }

        Calendar releaseDate = Calendar.getInstance();
        Calendar today1 = Calendar.getInstance();
        today1.set(today.getYear(), today.getMonth(), today.getDate());
        releaseDate.set(releaseDay.getYear(), releaseDay.getMonth(), releaseDay.getDate());

        long longReleaseDate = releaseDate.getTimeInMillis();
        long longToday = today1.getTimeInMillis();

        long i = longReleaseDate-longToday;
        int day = (int) i/(1000*60*60*24);

        if(i>0){
            return "D-" + day;
        }else if(i == 0){
            return "D-day";
        }

        //이미 개봉했으면 개봉일을 리턴한다다
        SimpleDateFormat format1 = new SimpleDateFormat("yy.MM.dd 개봉");
        return format1.format(releaseDay);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
