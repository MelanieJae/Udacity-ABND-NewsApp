package com.example.melanieh.newsfeedapp;

/**
 * Created by melanieh on 10/5/16.
 */

public class Article {

    // variables
    private String mURL;
    private String mPubDate;
    private String mTitle;
    private String mSection;

    public Article(String mPubDate, String mSection, String mTitle, String mURL) {
        this.mPubDate = mPubDate;
        this.mSection = mSection;
        this.mTitle = mTitle;
        this.mURL = mURL;
    }

    /** getters */
    public String getPubDate() {
        return mPubDate;
    }

    public String getSection() { return mSection; }

    public String getTitle() {
        return mTitle;
    }

    public String getURL() {
        return mURL;
    }
}
