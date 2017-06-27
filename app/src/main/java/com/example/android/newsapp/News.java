package com.example.android.newsapp;

/**
 * Created by kyle on 4/24/17.
 */

public class News {

    private String mTitle;

    private String mSection;

    private String mTime;

    private String mUrl;

    public News(String webTitle, String sectionName, String webPublicationDate, String webUrl) {

        mTitle = webTitle;
        mSection = sectionName;
        mTime = webPublicationDate;
        mUrl = webUrl;
    }

    public String getWebTitle() {
        return mTitle;
    }

    public String getSectionName() {
        return mSection;
    }


    public String getWebPublicationDate() {
        return mTime;
    }

    public String getWebUrl() {
        return mUrl;
    }
}
