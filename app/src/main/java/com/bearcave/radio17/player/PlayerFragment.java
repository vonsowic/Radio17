package com.bearcave.radio17.player;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.SparseArray;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bearcave.radio17.R;
import com.bearcave.radio17.RadioFragment;

import java.io.IOException;


public abstract class PlayerFragment extends RadioFragment
        implements  View.OnClickListener,
                    Player.OnStateListener{

    public PlayerFragment() {}

    public static final String SOURCE_KEY = "source-key-for-player";
    private static final String messageToAllPlayers = "all players: shut the fuck up";

    private String source;
    private Player player;
    private SparseArray<Runnable> buttonMap;

    private LocalBroadcastManager manager;
    private static IntentFilter pausePlayer = new IntentFilter(messageToAllPlayers);

    private ProgressBar loadingBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setSource(){
        source = getArguments().getString(SOURCE_KEY);
    }

    protected void initialize(){
        player = new Player(this);
        buttonMap = new SparseArray<>();

        manager = LocalBroadcastManager.getInstance(getContext());
        manager.registerReceiver(new OnShutTheFuckUpListener(), pausePlayer);

        loadingBar = (ProgressBar) getActivity().findViewById(R.id.loading_radio_bar);
    }

    public void playPause(){
        if ( player.isPlaying()){
            pause();
        } else {
            play();
        }
    }

    public void play(){
        try {
            player.play();
        } catch (IOException e) {
            notifyAboutInternetConnection();
        }
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }

    public void pause(){
        pauseAllPlayers();
    }

    public void stop(){
        player.stop();
    }

    @Override
    public void onClick(View v) {
        buttonMap.get(v.getId()).run();
    }

    /**
     * @param src url to audio
     * @return true when source is changed; false otherwise
     */
    public boolean setDataSource(String src){
        if (!(player.isCurrentlySet())){
            loadingBar.setVisibility(View.VISIBLE);
            player.setAudio(src);
            return true;
        }

        return false;
    }

    public boolean setDataSource(){
        return setDataSource(source);
    }

    @Override
    public void onPreparedStateListener() {
        loadingBar.setVisibility(View.GONE);

        try {
            player.play();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Brak lacznosci", Toast.LENGTH_LONG).show(); // TODO: remove when necessary
            notifyAboutInternetConnection();
        }
    }

    @Override
    public void noSourceSetListener() {
        player.setAudio(source);
    }

    /**
     * Set Runnable to id.
     * @param id
     * @param action
     */
    protected void put(int id, Runnable action){
        buttonMap.put(id, action );
    }

    private class OnShutTheFuckUpListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            player.pause();
        }
    }

    @Override
    public String onQuestionAboutSourceListener() {
        return source;
    }

    public void pauseAllPlayers(){
        manager.sendBroadcast(new Intent(messageToAllPlayers));
    }
}
