package com.example.android.newsapp;

public class News {

    private String title;
    private String section;
    private String dateTime;
    private String url;

    public News(String title, String section, String dateTime, String url) {
        this.title = title;
        this.section = section;
        this.dateTime = dateTime;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getUrl() {
        return url;
    }
}
