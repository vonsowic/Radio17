package com.bearcave.radio17.player;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bearcave.radio17.MainActivity;
import com.bearcave.radio17.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class PlayerActivity extends MainActivity {

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
                    new LoadTitle((TextView) findViewById(R.id.textTitle)).execute("http://37.187.247.31:8000/currentsong?sid=1");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void playRadio(View view) {
        if (PlayerService.isPlaying()){
            PlayerService.pause();
        } else {
            try {
                PlayerService.setAudio(getString(R.string.player_url));
            } catch (IOException e) {
                e.printStackTrace();
            }
            PlayerService.start();
        }
    }

    private class LoadTitle extends AsyncTask<String, Void, String> {

        TextView textView;

        public LoadTitle(TextView res){
            textView = res;
        }

        @Override
        protected String doInBackground(String... urls){
            Document doc;
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