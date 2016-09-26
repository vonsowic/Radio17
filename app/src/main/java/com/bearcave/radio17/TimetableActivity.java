package com.bearcave.radio17;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import me.biubiubiu.justifytext.library.JustifyTextView;

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

        new LoadTimetable(R.layout.content_timetable).execute("http://radio17.pl/ramowka/");


    }

    private class LoadTimetable extends AsyncTask<String, Void, Elements> {

        ProgressDialog mProgressDialog;
        LinearLayout layout;

        public LoadTimetable(int content_article) {
            this.layout = (LinearLayout) findViewById(R.id.timetableContent);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(TimetableActivity.this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();

        }

        @Override
        protected Elements doInBackground(String... urls) {

            Elements postContent;
            try {
                Document doc = Jsoup.connect(urls[0]).get();
                postContent = doc.getElementsByClass("post-content").first().children();
            } catch (IOException e) {
                return null;
            }

            return postContent;
        }

        @Override
        protected void onPostExecute(Elements result) {

            Elements elements;
            for ( int i =0; i< result.size(); i++){
                elements = result.get(i).children();
                for(int j=0; j<elements.size(); j++){
                    TextView tv = new TextView(TimetableActivity.this, null);
                    tv.setText(elements.get(j).text()+"\n");
                    tv.setTextSize(17);
                    layout.addView(tv);
                }

            }

            mProgressDialog.dismiss();
        }
    }

}
