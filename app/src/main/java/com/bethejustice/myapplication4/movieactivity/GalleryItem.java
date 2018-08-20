package com.bethejustice.myapplication4.movieactivity;

public class GalleryItem {
    final static int VIDEO = 1;
    final static int IMAGE = 0;

    private int type;
    private String url;

    public GalleryItem(int type, String url) {
        this.type = type;
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
