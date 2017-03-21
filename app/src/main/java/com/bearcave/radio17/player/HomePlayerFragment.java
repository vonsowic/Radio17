package com.bearcave.radio17.player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bearcave.radio17.R;

/**
 * Created by miwas on 21.03.17.
 */

public class HomePlayerFragment extends PlayerFragment {

    ImageButton playButt;
    ImageButton homeButt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player, container, false);
        player = new Player(this);

        playButt = (ImageButton) view.findViewById(R.id.player_play_button);
        playButt.setOnClickListener(this);

        homeButt = (ImageButton) view.findViewById(R.id.player_home_station_button);
        homeButt.setOnClickListener(this);

        return view;
    }

    @Override
    protected void initialize() {
        super.initialize();
        buttonMap.put(R.id.player_home_station_button, new OnHomeButtonClicked());
        buttonMap.put(R.id.player_play_button, new OnPlayButtonClicked());
    }

    private class OnPlayButtonClicked implements Runnable{

        @Override
        public void run() {
            playPause();
        }
    }

    private class OnHomeButtonClicked implements Runnable{

        @Override
        public void run() {
            onMainButtonClicked();
        }
    }
}
