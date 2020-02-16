package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;

import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>> {

    //URL zu The Guardian
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?api-key=07207c61-ec0e-434b-94ae-d2b87f6116cc";
    //Konstante für den NewsLoader
    private static final int NEWS_LOADER_ID = 1;
    //adapter für die News
    private NewsAdapter newsAdapter;
    //wird angezeigt, wenn die Liste leer ist
    private TextView emptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referenz zur ListView
        ListView newsListView = (ListView) findViewById(R.id.list);

        emptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(emptyStateTextView);

        //Erstellt einen neuen Adapter für die Liste der News
        newsAdapter = new NewsAdapter(this, new ArrayList<News>());

        //Setzt den Adapeter für das UI
        newsListView.setAdapter(newsAdapter);

        //setzt den ClickListener zur ListView, welche den Intent an einen Webbrowser schickt
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = newsAdapter.getItem(position);

                Uri newsUri = Uri.parse(currentNews.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                startActivity(websiteIntent);
            }
        });

        //checkt die Netzwerkverbindung
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        //bekommt Info des aktuellen Netzwerks
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //wenn eine Netzwerkverbindung besteht, werden die Daten abgerufen
        if (networkInfo != null && networkInfo.isConnected()) {
            //ruft Loader auf
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }
        //gibt Fehler aus
        else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        //erzeugt neuen Loader für die URL
        return new NewsLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        //versteckt das Loading Zeichen, wenn die Daten geladen sind
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        //gbit Meldung aus, wenn keine News vorhanden sind
        emptyStateTextView.setText(R.string.no_news);

        //leert die Liste, beim erneuten Laden
        newsAdapter.clear();

        //übergibt die Liste der News dem Adapter
        if (newsList != null && !newsList.isEmpty()) {
            newsAdapter.addAll(newsList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        this.newsAdapter.clear();
    }

}
