package com.bearcave.radio17.player;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bearcave.radio17.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeViewFragment extends Fragment implements View.OnClickListener{

    Thread loadSongTitleThread;

    public HomeViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_view, container, false);

        TextView textStation = (TextView) view.findViewById(R.id.stationName);
        textStation.setText("STACJA: "+getString(R.string.kanal_glowny));

        ImageButton button = (ImageButton) view.findViewById(R.id.home_button_listen);
        button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_button_listen:
                PlayerService.setAudio(getString(R.string.player_url));
                PlayerService.playPause();
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadSongTitleThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while ( true) {
                    //new LoadTitle((TextView) getView().findViewById(R.id.textTitle)).execute("http://37.187.247.31:8000/currentsong?sid=1");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        loadSongTitleThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            loadSongTitleThread.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //loadSongTitleThread.notify();
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
