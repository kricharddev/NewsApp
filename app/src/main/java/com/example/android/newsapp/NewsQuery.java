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
import java.util.List;

/**
 * Created by kyle on 4/24/17.
 */

public final class NewsQuery {


    public static String LOG_TAG = "Tag!";
    public NewsQuery() {
    }

    public static List<News> extractNews(String stringURL) {

        URL url = createURL(stringURL);

        String JSONResponse = null;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("NewsQuery", "Problem with parsing results", e);
        }

        List<News> news = extractFeaturesFromJSON(JSONResponse);

        return news;
    }

    private static URL createURL(String stringURL) {
        URL url;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error in creation of URL", e);
            return null;
        }
        Log.i(LOG_TAG, "created url object successfully: " + url);
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String JSONResponse = "";

        if (url == null) {
            Log.i(LOG_TAG, "The URL is NULL");
            return JSONResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* 10 seconds */);
            urlConnection.setConnectTimeout(15000 /* 15 seconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                JSONResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results", e);
        } finally {
            // Clean up connection
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            // Clean up stream
            if (inputStream != null) {
                inputStream.close();
            }
        }
        Log.i(LOG_TAG, "successfully returned JSON response");
        return JSONResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new
                    InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine(); // read the first line
            while (line != null) {
                // Loop through each line until it's null
                sb.append(line); // Append line
                line = reader.readLine(); // read next line
            }
        }
        Log.i(LOG_TAG, "successfully returned the inputstream.");
        return sb.toString();
    }

    private static List<News> extractFeaturesFromJSON(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            Log.i(LOG_TAG, "JSON string is empty.");
            return null;
        }

        try {
            List<News> news = new ArrayList<>();
            JSONObject base = new JSONObject(newsJSON);
            JSONObject response = base.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject currentArticle = results.getJSONObject(i);

                news.add(new News(
                        currentArticle.getString("webTitle"),
                        currentArticle.getString("sectionName"),
                        currentArticle.getString("webPublicationDate"),
                        currentArticle.getString("webUrl")
                ));
            }
            Log.i(LOG_TAG, "successfully returning List<Earthquake>.");
            return news;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problems parsing JSON", e);
        }
        Log.i(LOG_TAG, "returning null.");
        return null;
    }
}
