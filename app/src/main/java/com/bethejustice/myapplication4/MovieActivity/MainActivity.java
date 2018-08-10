package com.bethejustice.myapplication4.MovieActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bethejustice.myapplication4.AppHelper;
import com.bethejustice.myapplication4.MovieData.MovieInfo;
import com.bethejustice.myapplication4.MovieData.ResponseMovie;
import com.bethejustice.myapplication4.MovieData.ResponseMovieInfo;
import com.bethejustice.myapplication4.NetworkStatus;
import com.bethejustice.myapplication4.R;
import com.bethejustice.myapplication4.database.DatabaseHelper;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, InteractionListener{

    private DatabaseHelper db;
    Toolbar toolbar;
    ResponseMovie responseMovieList;
    ResponseMovieInfo movieInfoResponse;
    ViewPager pager;

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("영화목록");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 100) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("영화목록");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //데이터베이스 생성
        DatabaseHelper.openDatabase(getApplicationContext(), "cinema");

        //인터넷 연결확인
        NetworkStatus networkStatus = new NetworkStatus(getApplicationContext());
        int network = networkStatus.checkNetworkConnection();

        if(network == NetworkStatus.TYPE_NOT_CONNECTED){
            //데이터베이스에서 값 가져오기
        }

        //데이터받아오기
        if(network != NetworkStatus.TYPE_NOT_CONNECTED) {
            if (AppHelper.requestQueue == null) {
                AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
            }
            sendRequest();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.movieList) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent, 100);

        } else if (id == R.id.movieAPI) {

        } else if (id == R.id.movieBook) {

        } else if (id == R.id.settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class MoviePagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<Fragment> items = new ArrayList<Fragment>();

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
                setViewPager();
                DatabaseHelper.insertMovieList(responseMovieList.result);
            }
        } else {
            movieInfoResponse = gson.fromJson(response, ResponseMovieInfo.class);
            if(movieInfoResponse.code == 200){
                changeFragment( movieInfoResponse.result.get(0).id);
                DatabaseHelper.insertMovie(movieInfoResponse.result);
            }
        }
    }

    public void changeFragment(int id){
        /**
         * 인터넷이 연결되어 있지 않다면 여기서 movieInfo객체를 만들어서 새로은 프래그먼트를 만들때 번들로 넘겨주자.
         */
        MovieInfo movieInfo = movieInfoResponse.result.get(0);
        MainFragment fragment = MainFragment.newInstance(movieInfo);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)  // fragment 스택에서 관리, 뒤로가기버튼눌렀을때 여기로 돌아옴.
                .commit();
    }

    public void setViewPager(){
        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setOffscreenPageLimit(10);
        MoviePagerAdapter adapter = new MoviePagerAdapter(getSupportFragmentManager());

        /**
         * 인터넷이 연결되어 있지않다면 , 여기서 select 문을 구해서, movie 객체로 만들자,
         */
        for (int i = 0; i < responseMovieList.result.size(); i++) {
            MovieListFragment MLF = MovieListFragment.newInstance(responseMovieList.result.get(i));
            adapter.addItem(MLF);
        }
        pager.setAdapter(adapter);

    }

    public void changeAppTitle(String title){
        toolbar.setTitle(title);
    }
}
