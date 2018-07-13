package com.bethejustice.myapplication4;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bethejustice.myapplication4.MovieData.Movie;
import com.bethejustice.myapplication4.MovieData.ResponseMovie;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    ResponseMovie responseMovie;
    ViewPager pager;

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

        //데이터받아오기
        if(AppHelper.requestQueue == null){
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
            Log.d("main","requestQueue");
        }

        sendRequest();


        //viewPager


//        ViewPager pager =(ViewPager) findViewById(R.id.viewPager);
//        pager.setOffscreenPageLimit(10);
//        pager.setPageMargin(-100);
//        MoviePagerAdapter adapter = new MoviePagerAdapter(getSupportFragmentManager());
//
//        for(int i=0; i<responseMovie.movies.size(); i++){
//            MovieListFragment MLF = MovieListFragment.newInstance(responseMovie.movies.get(i));
//            adapter.addItem(MLF);
//        }
//        pager.setAdapter(adapter);
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
            startActivity(intent);
        } else if (id == R.id.movieAPI) {
            MainFragment mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();

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

        public void addItem(Fragment item){
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

    public void changeFragment(int id){

        Log.d("main", id+"");
        MainFragment fragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void changeTitle(String string){
        this.toolbar.setTitle(string);

    }

    public void sendRequest(){

        String url = "http://boostcourse-appapi.connect.or.kr:10000//movie/readMovieList";

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

    public void processResponse(String response){
        Gson gson = new Gson();
        responseMovie = gson.fromJson(response, ResponseMovie.class);
        if(responseMovie.code==200) {
            setViewPager();
        }
    }

    public void setViewPager(){
        pager =(ViewPager) findViewById(R.id.viewPager);
        pager.setOffscreenPageLimit(10);
        pager.setPageMargin(-100);
        MoviePagerAdapter adapter = new MoviePagerAdapter(getSupportFragmentManager());

        for(int i=0; i<responseMovie.result.size(); i++){
            MovieListFragment MLF = MovieListFragment.newInstance(responseMovie.result.get(i));
            adapter.addItem(MLF);
        }
        pager.setAdapter(adapter);
    }
}
