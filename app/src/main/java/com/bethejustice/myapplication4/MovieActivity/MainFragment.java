package com.bethejustice.myapplication4.MovieActivity;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bethejustice.myapplication4.CommentActivity.CommentItem;
import com.bethejustice.myapplication4.CommentActivity.CommentItemView;
import com.bethejustice.myapplication4.CommentActivity.CommentListActivity;
import com.bethejustice.myapplication4.MovieData.Movie;
import com.bethejustice.myapplication4.MovieData.MovieInfo;
import com.bethejustice.myapplication4.R;
import com.bethejustice.myapplication4.CommentActivity.CommentWriteActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment {
    int like = 15;
    int dislike = 1;
    boolean thumb_up_s = false;
    boolean thumb_down_s = false;
    float rating;

    Button thumbUpButton;
    Button thumbDownButton;
    TextView likeView;
    TextView dislikeView;
    RatingBar ratingBar;

    MainActivity mainActivity;

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
        mainActivity = new MainActivity();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 101) {
            // requestCode 101 commentWriteButton
            if (intent != null) {
                if (resultCode == RESULT_OK) {
                    String comment = intent.getStringExtra("comment");
                    Toast.makeText(getContext(), comment, Toast.LENGTH_LONG).show();
                }
            }

        } else if (requestCode == 102) {
            //requestCode 102 seeallButton;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        MovieInfo movieInfo = getArguments().getParcelable("movieInfo");

        //image view
        ImageView thumbView = (ImageView) rootView.findViewById(R.id.img_thumb);

        //button view
        thumbUpButton = (Button) rootView.findViewById(R.id.thumbUpButton);
        thumbDownButton = (Button) rootView.findViewById(R.id.thumbDownButton);
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        Button seeAllButton = (Button) rootView.findViewById(R.id.seeAllButton);
        Button commentWriteButton = (Button) rootView.findViewById(R.id.commentWriteButton);
        ListView list = (ListView) rootView.findViewById(R.id.commentList);


        //text view
        TextView titleView = rootView.findViewById(R.id.text_title);
        TextView dateView = rootView.findViewById(R.id.text_date);
        TextView gerneView = rootView.findViewById(R.id.text_genre_duration);
        likeView = (TextView) rootView.findViewById(R.id.text_like);
        dislikeView = (TextView) rootView.findViewById(R.id.text_dislike);
        TextView reservationRateView = rootView.findViewById(R.id.text_reservation_grade_rate);
        TextView userRatingView = rootView.findViewById(R.id.text_user_rating);
        TextView audienceView = rootView.findViewById(R.id.text_audience);
        TextView synopsisView = rootView.findViewById(R.id.text_synopsis);
        TextView directorView = rootView.findViewById(R.id.text_director);
        TextView actorView = rootView.findViewById(R.id.text_actor);

        /* 한줄평 부분
        CommentItemAdapter adapter = new CommentItemAdapter();
        list.setAdapter(adapter);
        adapter.addItem(new CommentItem("id", "hi", "hi", 1));
        adapter.addItem(new CommentItem("id", "hi", "hi", 1));
        adapter.addItem(new CommentItem("id", "hi", "hi", 1));
        adapter.notifyDataSetChanged();
        */

        /**
         * 내용 업데이트 부분
         *
         * like와 dislike의 경우 서버에 업데이트해야하는데
         *  업데이트 방법에 대해서 나와있지 않아 우선 변수로 선언해서 수정
         *  프래그먼트를 이동할때 값을 저장해는 방법에 대해서 생각해 봐야겠다.
         *
         */

        if (movieInfo != null) {

            Log.d("mainFrag", movieInfo.title + "");

            like = movieInfo.like;
            dislike = movieInfo.dislike;
            ratingBar.setRating(movieInfo.user_rating);
            likeView.setText(like + "");
            dislikeView.setText(dislike + "");

            Glide.with(container).load(movieInfo.getThumb()).into(thumbView);
            titleView.setText(movieInfo.getTitle());
            dateView.setText(movieInfo.getDate()+" 개봉");
            gerneView.setText(movieInfo.getGenre() + " / " + movieInfo.getDuration() + "분");
            reservationRateView.setText(String.format(movieInfo.getReservation_grade() + "위 " + movieInfo.getReservation_rate()) + "%");
            userRatingView.setText(movieInfo.getUser_rating() + "");
            audienceView.setText(movieInfo.getAudience() + "");
            synopsisView.setText(movieInfo.getSynopsis());
            directorView.setText(movieInfo.getDirector());
            actorView.setText(movieInfo.getActor());
        }


        thumbDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!thumb_down_s) {

                    if (thumb_up_s) {
                        like--;
                        thumb_up_s = false;
                        thumbUpButton.setBackgroundResource(R.drawable.ic_thumb_up);
                        dislike++;
                        thumb_down_s = true;
                        thumbDownButton.setBackgroundResource(R.drawable.ic_thumb_down_selected);

                    } else {
                        dislike++;
                        thumb_down_s = true;
                        thumbDownButton.setBackgroundResource(R.drawable.ic_thumb_down_selected);


                    }

                } else {

                    thumb_down_s = false;
                    dislike--;
                    thumbDownButton.setBackgroundResource(R.drawable.ic_thumb_down);

                }

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
                        like++;
                        thumb_up_s = true;
                        thumbUpButton.setBackgroundResource(R.drawable.ic_thumb_up_selected);
                    } else {
                        like++;
                        thumb_up_s = true;
                        thumbUpButton.setBackgroundResource(R.drawable.ic_thumb_up_selected);
                    }

                } else {

                    thumb_up_s = false;
                    like--;
                    thumbUpButton.setBackgroundResource(R.drawable.ic_thumb_up);

                }

                likeView.setText(Integer.toString(like));
                dislikeView.setText(Integer.toString(dislike));
            }
        });


        seeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "seeAllButton was cliked", Toast.LENGTH_LONG).show();

                Log.d("intent", "fragment");
                Intent intent = new Intent(getContext(), CommentListActivity.class);
                startActivity(intent);

            }
        });

        commentWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "CommentWriteButton was cliked" + rating, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), CommentWriteActivity.class);
                intent.putExtra("rating", rating);
                startActivity(intent);
            }
        });

        return rootView;
    }

    class CommentItemAdapter extends BaseAdapter {
        ArrayList<CommentItem> commentItems = new ArrayList<>();

        public void addItem(CommentItem item) {
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

            CommentItemView view = null;
            if (convertView == null) {
                view = new CommentItemView(getContext());
            } else {
                view = (CommentItemView) convertView;
            }

            CommentItem item = commentItems.get(position);


            return view;
        }
    }
}
