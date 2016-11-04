package com.example.melanieh.newsfeedapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
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
import java.util.List;

/**
 * Created by melanieh on 10/5/16.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private String LOG_TAG = ArticleLoader.class.getName();
    String mURLString;
    List<Article> articles;
    Article article;

    public ArticleLoader(Context context, String urlString) {
        super(context);
        this.mURLString = urlString;

    }

    @Override
    public ArrayList<Article> loadInBackground() {

        URL url = null;
        String jsonResponse = "";
        // Create the URL object from the string passed to the loader constructor and via forceLoad method
        // and then make the http connection request.
        url = createUrl(mURLString);
        jsonResponse = makeHttpRequest(url);

        ArrayList<Article> articles = obtainArticleData(jsonResponse);

        return articles;
    }

    /**
     * creates URLs from String URL addresses
     */

    public URL createUrl(String urlString) {

        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }


    /** creates the url HttpConnection and then opens it, takes in the input stream
     * associated with the connection and then calls readConvertStream helper method in this class
     * to convert that stream into a string-formatted JSON response
     * @param url
     * @return
     */
    public String makeHttpRequest(URL url) {

        String jsonResponse = "";

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
                jsonResponse = readConvertStream(inputStream);
            }

            if (connection != null) {
                connection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
        }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error opening HTTPURL connection");
        }

        return jsonResponse;
    }


    /** converts InputStream into a String-formatted json response from which the news article
     * info is extracted
     * @param inputStream
     * @return
     * @throws IOException (caught where it is called, in the makeHttpRequest method above)
     */
    private String readConvertStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader
                    (inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /** fetches data from the JSON response and parses it into a form for display in the UI **/

    public ArrayList<Article> obtainArticleData(String baseJSONresponse) {

        // initial null check of base JSON response string
        if (baseJSONresponse == null) {
            return null;
        }

        ArrayList<Article> articles = new ArrayList<Article>();

        try {

            JSONObject baseJSONObject = new JSONObject(baseJSONresponse);
            JSONObject responseJSONObject = baseJSONObject.getJSONObject("response");
            JSONArray resultsJSONArray = responseJSONObject.getJSONArray("results");
            Log.v(LOG_TAG, "results array length= " + resultsJSONArray.length());

            if (resultsJSONArray.length() > 0) {

                for (int i = 0; i < resultsJSONArray.length(); i++) {
                    JSONObject articleJSONObject = resultsJSONArray.getJSONObject(i);
                    // convert results into their proper formats
                    String urlString = articleJSONObject.getString("webUrl");
                    String pubDateJSONString = articleJSONObject.getString("webPublicationDate");
                    String pubDate = parseDateTimeString(pubDateJSONString);
                    String title = articleJSONObject.getString("webTitle");
                    String section = articleJSONObject.getString("sectionName");

                    article = new Article(pubDate, section, title, urlString);
                    articles.add(article);
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing JSON response");
        }
        return articles;
    }


    /* Date and time string formatting method returning a readable date and time format to the list view */
    private String parseDateTimeString(String dateTime) {
        // e.g. "2016-09-15T13:00:50Z" (standard format of webPubDate in Guardian API JSON results)
        String yearSubString = dateTime.substring(0,4);
        String monthSubString = dateTime.substring(5,7);
        String daySubString = dateTime.substring(8,10);
        String timeSubString = dateTime.substring(11,19);

        String parsedDateTimeString = yearSubString + "-" + monthSubString + "-" + daySubString
                + " " + timeSubString + " UTC";

        return parsedDateTimeString;
    }


}










