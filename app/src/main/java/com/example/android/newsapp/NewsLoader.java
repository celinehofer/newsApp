package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String url;
    private QueryUtils queryUtils;

    //erstellt einen neuen Loader
    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
        this.queryUtils = new QueryUtils();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {

        //erstellt URL Objekt
        URL url = this.queryUtils.createUrl(this.url);

        //führt HTTP Request an die URL aus und erhält ein JSON als Response
        String jsonResponse = "";
        try {
            if (url != null) {
                jsonResponse = this.queryUtils.makeHttpRequest(url);
            } else {
                return null;
            }
        } catch (IOException e) {
            Log.e("DoInBackground", "Problem parsing the news JSON results", e);
        }

        return this.queryUtils.extractFeatureFromJson(jsonResponse);
    }
}
