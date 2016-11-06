package com.example.melanieh.newsfeedapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melanieh on 10/6/16.
 */

public class ArticleAdapter extends ArrayAdapter {

    List<Article> articles = new ArrayList<Article>();

    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Article currentArticle = (Article)getItem(position);

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // TextViews in list_item layout where the article info appears
        TextView sectionView = (TextView) listItemView.findViewById(R.id.section);
        sectionView.setText(currentArticle.getSection());

        TextView pubDateView = (TextView) listItemView.findViewById(R.id.pub_date);
        pubDateView.setText(currentArticle.getPubDate());

        TextView pubTitleView = (TextView) listItemView.findViewById(R.id.title);
        pubTitleView.setText(currentArticle.getTitle());

        String buttonText = "Read Story";
        Button pubURLButton = (Button) listItemView.findViewById(R.id.url_string);
        pubURLButton.setText(buttonText);

        pubURLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri articleURL = Uri.parse(currentArticle.getURL());
                Intent viewArticle = new Intent(Intent.ACTION_VIEW, articleURL);
                getContext().startActivity(viewArticle);
            }
        });

        return listItemView;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    /** analogous 'setter' method for the arraylist */
    public void setEarthquakes(ArrayList<Article> articleData) {
        articles.addAll(articleData);
        notifyDataSetChanged();
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
