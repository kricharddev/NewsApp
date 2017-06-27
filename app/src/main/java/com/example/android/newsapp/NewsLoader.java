package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by kyle on 4/24/17.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String LOG_TAG = NewsLoader.class.getName();
    private String mURL;

    public NewsLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i(LOG_TAG, "force load");
    }

    @Override
    public List<News> loadInBackground() {
        if (mURL == null) {
            Log.i(LOG_TAG, "mURL is null.");
            return null;
        }

        List<News> news = NewsQuery.extractNews(mURL);
        Log.i(LOG_TAG, "Returning News Data");
        return news;
    }
}
