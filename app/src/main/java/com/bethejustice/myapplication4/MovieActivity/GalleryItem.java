package com.bethejustice.myapplication4.MovieActivity;

public class GalleryItem {
    final int Video = 1;
    final int IMAGE = 0;

    private int distinct;
    private String url;

    public GalleryItem(int distinct, String url) {
        this.distinct = distinct;
        this.url = url;
    }

    public int getDistinct() {
        return distinct;
    }

    public void setDistinct(int distinct) {
        this.distinct = distinct;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
