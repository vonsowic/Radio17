package com.bearcave.radio17;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


public class TimetableActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_timetable, null, false);
        drawer.addView(contentView, 0);

        Intent i = getIntent();
        String title = i.getStringExtra("article_list_category");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);

        new LoadArticle(this, (LinearLayout)findViewById(R.id.timetableContent)).execute("http://radio17.pl/ramowka/");
    }
}
