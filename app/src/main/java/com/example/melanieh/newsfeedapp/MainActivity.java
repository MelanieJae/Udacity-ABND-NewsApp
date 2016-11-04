package com.example.melanieh.newsfeedapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.empty;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Article>> {

    // log tag
    private String LOG_TAG = MainActivity.class.getSimpleName();

    List<Article> articles;
    ArrayAdapter<Article> articleAdapter;
    Button pubURLButton;
    String apiKeyValue;
    String fromDateValue;
    String tagValue;
    String queryValue;
    ProgressBar progressBar;
    TextView emptyView;
    EditText sectionInputView;

    // sample API queries
    // test keys
    // QUERY 1 will produce latest news; QUERY 2 will produce a specific search result for political
    // news
    private final String TEST_QUERY_1=
            "https://content.guardianapis.com/search?api-key=3f9dfa02-174c-47c9-9884-45fbbdbd904e";
    private final String TEST_QUERY_2=
            "http://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2014-01-01&api-key=test";

    // EmptyView test query
    private final String EMPTY_QUERY="http://content.guardianapis.com/search?q=null";

    // Uri builder initializations
    private final String BASE_URL="http://content.guardianapis.com/search?";

    private String QUERY_URL=new String();

    // loader ID
    private static final int ARTICLE_LOADER_ID=1;

    /** settings menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_refresh) {

            ListView listView = (ListView) findViewById(R.id.list_view);
            articleAdapter = new ArticleAdapter(MainActivity.this, new ArrayList<Article>());
            listView.setAdapter(articleAdapter);

            getSupportLoaderManager().initLoader(ARTICLE_LOADER_ID, null, this).forceLoad();
        }
        return super.onOptionsItemSelected(item);

    }

    /** main layout list of articles */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView instructText = (TextView) findViewById(R.id.search_instructions);

        //      EditText view for user input
        sectionInputView = (EditText)findViewById(R.id.section_input);

        // initializing and binding adapter to listView
        ListView listView = (ListView)findViewById(R.id.list_view);
        articleAdapter = new ArticleAdapter(MainActivity.this, new ArrayList<Article>());
        listView.setAdapter(articleAdapter);

        // listView's emptyView
        emptyView = (TextView)findViewById(empty);
        listView.setEmptyView(emptyView);

        final Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //hide text, search field and search button when results are displayed
                instructText.setVisibility(View.GONE);
                sectionInputView.setVisibility(View.GONE);
                searchButton.setVisibility(View.GONE);

                // dismiss keyboard after user hits the search button
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(sectionInputView.getWindowToken(), 0);

                // adapter initialization call for loader
                ListView listView = (ListView)findViewById(R.id.list_view);

                // check internet connection; if none, hide progress indicator and show
                // "no internet connection" message
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {

                    // fetch data...
                    articleAdapter = new ArticleAdapter(MainActivity.this, new ArrayList<Article>());
                    listView.setAdapter(articleAdapter);

                    // initiate the loader via the loader manager interface
                    Log.v(LOG_TAG, "testing: initLoader called...");

                    // execute loader
                    getSupportLoaderManager().initLoader(ARTICLE_LOADER_ID, null, MainActivity.this)
                            .forceLoad();

                } else {
                    // display error and hide progress bar when there is no internet connection
                    emptyView.setText(R.string.no_connection_error);
                }

            }
        });

    }

    /** loader manager methods */


    // creates new Loader instance and calls loadInBackground to return results via the loader
    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {

        // add uri builder for API query here
        Uri baseURi = Uri.parse(BASE_URL);
        // Search keyword field
        // setting user keyword as the query value rather than tag value as
        // it will typically return more results and avoid the risk of submitting
        // a keyword as a tag that does not exist, causing a MalformedJSON exception.
        String apiKeyValue=BuildConfig.GUARDIAN_API_KEY;
        String queryValue = sectionInputView.getText().toString();
        Uri.Builder uriBuilder = baseURi.buildUpon();
        uriBuilder.appendQueryParameter("q", queryValue);
        uriBuilder.appendQueryParameter("api-key", apiKeyValue);
        Log.v(LOG_TAG, uriBuilder.toString());

        return new ArticleLoader(this, uriBuilder.toString());

    }

    // updates UI
    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {

        articleAdapter.clear();
        if (articles != null && !articles.isEmpty()) {
            articleAdapter.addAll(articles);

        } else {
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText(R.string.empty_list_view);

        }
    }

    // cleans out all the data for a new use
    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        articleAdapter.clear();
    }

}
