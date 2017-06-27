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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    private static final String SEARCH_URL =
            "http://content.guardianapis.com/search?section=technology&api-key=test";

    private static final int NEWS_LOADER_ID = 0;

    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list);

        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentArticle = mAdapter.getItem(position);

                Uri newsUri = Uri.parse(currentArticle.getWebUrl());

                // Creating intent to view the Article's Web Page
                Intent detailsIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                startActivity(detailsIntent);
            }
        });

        // Checking if internet is available.
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(NEWS_LOADER_ID, null, this);
            Log.i(LOG_TAG, "start Loader");
        } else {
            Log.i(LOG_TAG, "skipping loader, internet = null.");
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        Uri baseUri = Uri.parse(SEARCH_URL);
        Uri.Builder ub = baseUri.buildUpon();

        Log.i(LOG_TAG, "onCreateLoader");
        return new NewsLoader(this, ub.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        mAdapter.clear();

        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
            Log.i(LOG_TAG, "onLoadFinished: adding to mAdapter.");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
        Log.i(LOG_TAG, "clearing mAdapter.");
    }
}
