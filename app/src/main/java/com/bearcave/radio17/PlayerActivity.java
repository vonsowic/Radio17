package com.bearcave.radio17;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class PlayerActivity extends MainActivity {

    static Intent radioIntent = null;
    static boolean isPlaying = false;

    String url = "http://tolo.me:8000/;";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_player, null, false);
        drawer.addView(contentView, 0);

        TextView textStation = (TextView) findViewById(R.id.stationName);
        textStation.setText("STACJA: "+getString(R.string.kanal_glowny));

        new Thread(new Runnable() {
            @Override
            public void run() {
                while ( true) {
                    new LoadTitle((TextView) findViewById(R.id.textTitle)).execute("http://tolo.me:8000/currentsong?sid=1");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.station_main) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void playRadio(View view) {

        if ( isPlaying ) {
            stopService(radioIntent);
            isPlaying = false;
        } else {

            radioIntent = new Intent(this, PlayerService.class);
            radioIntent.putExtra("audio_src", url);
            isPlaying = true;
            startService(radioIntent);
        }
    }

    private class LoadTitle extends AsyncTask<String, Void, String> {

        TextView textView;

        public LoadTitle(TextView res){
            textView = res;
        }

        @Override
        protected String doInBackground(String... urls){
            Document doc=null;
            try {
                doc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                return getString(R.string.title_not_available);
            }
            return doc.text();
        }

        @Override
        protected void onPostExecute(String title){
            textView.setText(title);

        }
    }


}