package com.bethejustice.myapplication4.movieactivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bethejustice.myapplication4.AppHelper;
import com.bethejustice.myapplication4.moviedata.Movie;
import com.bethejustice.myapplication4.moviedata.MovieInfo;
import com.bethejustice.myapplication4.moviedata.ResponseMovie;
import com.bethejustice.myapplication4.moviedata.ResponseMovieInfo;
import com.bethejustice.myapplication4.NetworkState;
import com.bethejustice.myapplication4.R;
import com.bethejustice.myapplication4.database.DatabaseHelper;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;

import java.util.ArrayList;

/** 20180813 todolist
 *
 *  전체적인 컨벤션 맞추기
 *  자바파일이나 리소스 메소드의 이름 재정의하기
 *
 *  mainactivity에서 fragment의 화면전환 수정하기 0815
 *  좋아요 버튼 관련 리팩토링 0814
 *  데이터베이스 관련 코드 정리
 *
 *  메소드의 연계성 줄이기 및 중복된 코드 수정하기
 *  주석달기
 *
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, InteractionListener{

    private final int ORDER_OPTION_CURATION = 0;
    private final int ORDER_OPTION_RESERVATION = 1;
    private final int ORDER_OPTION_DATE = 2;

    Toolbar toolbar;
    ResponseMovie responseMovieList;
    ResponseMovieInfo movieInfoResponse;
    ViewPager pager;
    NetworkState networkState;

    Animation animation_down;
    Animation animation_up;

    int orderOptionState = ORDER_OPTION_CURATION;
    ImageView orderOptionView;
    View orderOptionList;
    boolean orderOptionIsShown = false;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 100) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            //화면전환 수정중
            transaction.commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("영화목록");
        setSupportActionBar(toolbar);
        toolbar.setElevation(0);

        //핀치투줌
        Stetho.initializeWithDefaults(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //데이터베이스 생성
        DatabaseHelper.openDatabase(getApplicationContext(), "cinema");

        //인터넷 연결확인
        networkState = new NetworkState(getApplicationContext());
        int network = networkState.checkNetworkConnection();

        if(network == NetworkState.TYPE_NOT_CONNECTED){
            setViewPager(network);
        }

        //데이터받아오기
        if(network != NetworkState.TYPE_NOT_CONNECTED) {
            if (AppHelper.requestQueue == null) {
                AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
            }
            sendRequest();
        }

        // 정렬순서 옵션
        orderOptionView = toolbar.findViewById(R.id.btn_order);
        orderOptionList = findViewById(R.id.menu_list);

        ImageView order1 = orderOptionList.findViewById(R.id.ic_order_reservation);
        ImageView order2 = orderOptionList.findViewById(R.id.ic_order_curation);
        ImageView order3 = orderOptionList.findViewById(R.id.ic_order_date);

        order1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderOptionState != ORDER_OPTION_RESERVATION){
                    orderOptionView.setImageResource(R.drawable.order11);
                    orderOptionState = ORDER_OPTION_RESERVATION;
                }
                orderOptionIsShown = !orderOptionIsShown;
                orderOptionList.startAnimation(animation_up);
            }
        });

        order2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderOptionState != ORDER_OPTION_CURATION){
                    orderOptionView.setImageResource(R.drawable.order22);
                    orderOptionState = ORDER_OPTION_CURATION;
                }
                orderOptionIsShown = !orderOptionIsShown;
                orderOptionList.startAnimation(animation_up);
            }
        });

        order3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderOptionState != ORDER_OPTION_DATE){
                    orderOptionView.setImageResource(R.drawable.order33);
                    orderOptionState = ORDER_OPTION_DATE;
                }
                orderOptionIsShown = !orderOptionIsShown;
                orderOptionList.startAnimation(animation_up);
            }
        });

        animation_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_down);
        animation_down.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                orderOptionList.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_up);
        animation_up.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                orderOptionList.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        orderOptionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(orderOptionIsShown){
                    orderOptionList.startAnimation(animation_up);
                }else{
                    orderOptionList.startAnimation(animation_down);
                }

                orderOptionIsShown = !orderOptionIsShown;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.movieList) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, 100);

        } else if (id == R.id.movieAPI) {

        } else if (id == R.id.movieBook) {

        } else if (id == R.id.settings) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("영화목록");
        orderOptionView.setVisibility(View.VISIBLE);
        orderOptionIsShown = false;
    }

    /**
     *  메소드 부분
     */

    public void sendRequest() {
        String url = "http://boostcourse-appapi.connect.or.kr:10000//movie/readMovieList";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponse(response, 1);

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

    public void sendRequest(int movieId) {
        String url = "http://boostcourse-appapi.connect.or.kr:10000//movie/readMovie?id=" + movieId;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //index 1 => 영화목록, 2 => 상세정보
                        processResponse(response, 2);
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

    public void processResponse(String response, int index){
        Gson gson = new Gson();
        if (index == 1) {
            responseMovieList = gson.fromJson(response, ResponseMovie.class);
            if (responseMovieList.code == 200) {
                setViewPager(networkState.checkNetworkConnection());
                DatabaseHelper.insertMovieList(responseMovieList.result);
            }
        } else {
            movieInfoResponse = gson.fromJson(response, ResponseMovieInfo.class);
            if(movieInfoResponse.code == 200){
                changeFragment(movieInfoResponse.result.get(0).getId());
                DatabaseHelper.insertMovie(movieInfoResponse.result);
            }
        }
    }

    public void changeFragment(int id){
        MovieInfo movieInfo = null;
        if(networkState.checkNetworkConnection()== NetworkState.TYPE_NOT_CONNECTED){
            Cursor movieInfoCursor = DatabaseHelper.selectMovie(id);
            if(movieInfoCursor.moveToNext()) {
                movieInfo = new MovieInfo(movieInfoCursor);
            }
        }else{
            movieInfo = movieInfoResponse.result.get(0);
        }
        if(movieInfo!=null) {
            MainFragment fragment= MainFragment.newInstance(movieInfo);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .addToBackStack(null)  // fragment 스택에서 관리, 뒤로가기버튼눌렀을때 여기로 돌아옴.
                    .commit();
        }
    }

    public void setViewPager(int networkConnectionState){
        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setOffscreenPageLimit(10);
        MoviePagerAdapter adapter = new MoviePagerAdapter(getSupportFragmentManager());

        if(networkConnectionState == NetworkState.TYPE_NOT_CONNECTED){
            Cursor movieListCursor = DatabaseHelper.selectMovieList();

            while(movieListCursor.moveToNext()){
                Movie temp = new Movie(movieListCursor);
                MovieListFragment MLF = MovieListFragment.newInstance(temp);
                adapter.addItem(MLF);
            }
        }

        if(networkConnectionState != NetworkState.TYPE_NOT_CONNECTED) {
            for (int i = 0; i < responseMovieList.result.size(); i++) {
                MovieListFragment MLF = MovieListFragment.newInstance(responseMovieList.result.get(i));
                adapter.addItem(MLF);
            }
        }
        pager.setAdapter(adapter);
    }

    public class MoviePagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<>();

        public MoviePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addItem(Fragment item) {
            items.add(item);
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }

    public void changeAppTitle(String title){
        toolbar.setTitle(title);
    }

    public void removeOrderMenus(boolean orderOptionIsShown) {
        if (orderOptionIsShown) {
            orderOptionView.setVisibility(View.INVISIBLE);
        }else{
            orderOptionView.setVisibility(View.VISIBLE);
        }
    }
}
