package com.bearcave.radio17.player;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bearcave.radio17.MainActivity;
import com.bearcave.radio17.R;
import com.bearcave.radio17.exceptions.NoInternetConnectionException;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Objects;


public class HomeViewFragment extends Fragment implements View.OnClickListener{

    Thread loadSongTitleThread;
    TextView songTitleTextView;

    public HomeViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: it's so ugly, that it makes onion cry. There must another solution for loading title.
        loadSongTitleThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final String title;
                    String tmpTitle;
                    try {
                        tmpTitle = Jsoup.connect("http://37.187.247.31:8000/currentsong?sid=1").get().text();
                    } catch (IOException e) {
                        tmpTitle = getActivity().getString(R.string.title_not_available);
                    }
                    title = tmpTitle;

                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                songTitleTextView.setText(title);
                            }
                        });
                    } catch (NullPointerException e){ }


                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_view, container, false);
        songTitleTextView = (TextView) view.findViewById(R.id.text_song_title);

        TextView textStation = (TextView) view.findViewById(R.id.stationName);
        textStation.setText("STACJA: "+getString(R.string.kanal_glowny));

        ImageButton button = (ImageButton) view.findViewById(R.id.home_button_listen);
        button.setOnClickListener(this);

        getActivity().setTitle(
                getString(R.string.app_name)
        );


        loadSongTitleThread.start();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_button_listen:
                Player.setAudio(getString(R.string.player_url));
                try {
                    Player.playPause();
                } catch (IOException e) {
                    Toast.makeText(getContext(), R.string.no_internet_conn_notification, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        loadSongTitleThread.interrupt();
    }
}
