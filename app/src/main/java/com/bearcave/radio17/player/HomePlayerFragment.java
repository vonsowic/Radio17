package com.bearcave.radio17.player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.bearcave.radio17.R;
import java.io.IOException;

/**
 * @author Michał Wąsowicz
 */
public class HomePlayerFragment extends PlayerFragment {

    private ImageButton homeButt;
    private ImageButton playButt;

    private static final String MAIN_STATION_URL = "http://37.187.247.31:8000/;";

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
    public void onDestroyView() {
        super.onDestroyView();
        getPlayer().stopListeningToAllPlayers();
    }

    @Override
    protected void initialize() {
        super.initialize();
        put(R.id.player_home_station_button, new OnHomeButtonClicked());
        put(R.id.player_play_button, new OnPlayButtonClicked());

        try {
            if (isPlaying()) {
                onPlayChangeIcons();
            }
        } catch (NullPointerException e){}
    }

    @Override
    protected void onPlayChangeIcons() {
        playButt.setImageResource(R.drawable.ic_pause_black_24dp);
    }

    @Override
    protected void onPauseChangeIcons() {
        playButt.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    }

    @Override
    public void onPlayerNotSetListener() {
        try {
            getPlayer().forcedPlay();
        } catch (IOException e) {
            notifyAboutInternetConnection();
        }
    }

    @Override
    public void onFinishedStartingService() {
        getPlayer().startListeningToAllPlayers();
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



    public void onMainButtonClicked(){
        if (isPlaying()) {
            pause();
        } else {
            setDataSource(MAIN_STATION_URL);
            play();
        }
    }
}
