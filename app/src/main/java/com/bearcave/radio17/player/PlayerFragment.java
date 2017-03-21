package com.bearcave.radio17.player;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.bearcave.radio17.R;
import com.bearcave.radio17.RadioFragment;

import java.io.IOException;


public class PlayerFragment extends RadioFragment
        implements  View.OnClickListener,
                    Player.OnStateListener{

    public PlayerFragment() {}

    private String source;
    public static final String SOURCE_KEY = "source-key-for-player";

    private Player player;

    ImageButton homeButt;
    ImageButton playButt;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player, container, false);
        player = new Player(this);
        source = getArguments().getString(SOURCE_KEY);

        homeButt = (ImageButton) view.findViewById(R.id.player_play_button);
        homeButt.setOnClickListener(this);

        playButt = (ImageButton) view.findViewById(R.id.player_home_station_button);
        playButt.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_play_button:
                playPause();
                break;

            case R.id.player_home_station_button:
                onMainButtonClicked();
                break;
        }
    }

    public void onMainButtonClicked(){
        try {
            if (player.isPlaying()) {
                player.pause();
                setDataSource(source);

            } else {
                player.play();
            }
        } catch (IOException e){
            notifyAboutInternetConnection();
        }
    }

    public void playPause(){
        try {
            player.playPause();
        } catch (IOException e) {
            notifyAboutInternetConnection();
        }
    }

    /**
     * @param source url to audio
     * @return true when source is changed; false otherwise
     */
    public boolean setDataSource(String source){
        if (!source.equals(player.getCurrentlyPlayed())){
            player.setAudio(source);
            return true;
        }

        return false;
    }

    @Override
    public void onPreparedStateListener() {
        try {
            player.play();
        } catch (IOException e) {
            notifyAboutInternetConnection();
        }
    }

    @Override
    public void noSourceSetListener() {
        player.setAudio(source);
    }
}
