package com.bethejustice.myapplication4.movieactivity;

public interface InteractionListener {
    void changeAppTitle(String title);
    void removeOrderMenus(boolean onOff);
    void sendRequest(int userId);
    void changeFragment(int movieId);
}
