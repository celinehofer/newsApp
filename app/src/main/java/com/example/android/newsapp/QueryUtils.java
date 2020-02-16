package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public QueryUtils() {
    }

        //erstellt ein neues URL Objekt
        public URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Problem building the URL ", e);
            }
            return url;
    }

    //Führt HHTP Request an die gegebene URL aus und erhält als Response einen String
    public String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000  /* milliseconds */);
            urlConnection.setConnectTimeout(15000  /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //bei erfolgreichem Request kommt Status 200 zurück
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //Konvertiert den gesamten JSON Reseponse in einen String
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //Gibt eine Liste von News-Objekten zurück, von der JSON Response
    public ArrayList<News> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        //erstellt ArrayListe, in welche dann News hinzugefügt werden können
        ArrayList<News> newsList = new ArrayList<>();

        //versucht das JSON zu zergliedern, wenn nicht möglich wird eine Exception zurück gegeben
        try {

            //erstellt ein JSON Objekt des Response Strings
            JSONObject json = new JSONObject(newsJSON);
            JSONObject response = json.getJSONObject("response");

            //Exportiert das JSON Array mit dem Schlüssel "results"
            JSONArray results = response.getJSONArray("results");

            //für jede News im Array wird ein Objekt erstellt
            for (int i = 0; i < results.length(); i++) {
                JSONObject currentNews = results.getJSONObject(i);

                //exportiert die einzelnen Items für eine News
                String title = currentNews.getString("webTitle");
                String section = currentNews.getString("sectionName");
                String date = currentNews.getString("webPublicationDate");
                String url = currentNews.getString("webUrl");

                //erstellt eine NewsList und fügt die Items hinzu
                News news = new News(title, section, date, url);
                newsList.add(news);
            }


        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        //Gibt NewsList zurück
       return newsList;
    }

}
