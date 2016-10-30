package com.example.melanieh.newsfeedapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by melanieh on 10/5/16.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private String LOG_TAG=ArticleLoader.class.getName();
    String mURLString;
    List<Article> articles;

    public ArticleLoader(Context context, String urlString) {
        super(context);
        this.mURLString=urlString;

    }

    @Override
    public List<Article> loadInBackground() {

        URL url = null;
        String jsonResponse="";
        // Create the URL object from the string passed to the loader constructor and via forceLoad method
        // and then make the http connection request.
        try {
            url = new URL(mURLString);
            jsonResponse = makeHttpRequest(url);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error creating URL", e);
        }

        // make the HTTP request
        //obtainData(String urlString)
        List<Article> articles = obtainArticleData(jsonResponse);




        return null;
    }

    public String makeHttpRequest(URL url) {
        String jsonResponse="";

        if (url == null) {
            return jsonResponse;
        }

        // if URL creation was successful, read inputstream and attempt to parse the response
        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                jsonResponse = readFrom
            }

            } catch (IOException e) {
            Log.e(LOG_TAG, "Error opening HTTPURL connection");
        }





        return jsonResponse;
    };

}
