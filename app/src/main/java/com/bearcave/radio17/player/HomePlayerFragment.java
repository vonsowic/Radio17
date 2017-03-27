package com.bearcave.radio17.player;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bearcave.radio17.R;

/**
 * Created by miwas on 21.03.17.
 */

public class HomePlayerFragment extends PlayerFragment {

    private ImageButton homeButt;
    private ImageButton playButt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        setSource();

        playButt = (ImageButton) view.findViewById(R.id.player_play_button);
        playButt.setOnClickListener(this);

        homeButt = (ImageButton) view.findViewById(R.id.player_home_station_button);
        homeButt.setOnClickListener(this);

        initialize();

        return view;
    }

    @Override
    protected void initialize() {
        super.initialize();
        put(R.id.player_home_station_button, new OnHomeButtonClicked());
        put(R.id.player_play_button, new OnPlayButtonClicked());
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

    @Override
    public void play() {
        super.play();
        playButt.setImageResource(R.drawable.ic_pause_black_24dp);
    }

    @Override
    public void pause() {
        super.pause();
        playButt.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    }
}
