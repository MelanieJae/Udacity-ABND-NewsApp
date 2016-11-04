package com.example.melanieh.newsfeedapp;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.List;

import static android.R.string.no;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Article>>{

    List<Article> articles;
    ArrayAdapter<Article> articleAdapter;

    // sample API queries
    private final String TEST_QUERY_1=
            "http://content.guardianapis.com/search?q=debates&api-key=test";
    private final String TEST_QUERY_2=
            "http://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2014-01-01&api-key=test";

    // loader ID
    private static final int ARTICLE_LOADER_ID=1;

    /** main layout list of articles */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLoaderManager().initLoader()
    }

    /** loader methods */

    public void onStartLoading() {

    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {

        // add uri builder for API query here

        return new ArticleLoader(this, TEST_QUERY_1);
    }



    // updates UI
    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {

        ArrayAdapter<Article> articleAdapter = new ArrayAdapter<Article>(this, R.layout.list_item,
                articles);

    }



}
