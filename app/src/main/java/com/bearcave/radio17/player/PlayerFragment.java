package com.bearcave.radio17.player;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;
import android.widget.ProgressBar;

import com.bearcave.radio17.R;
import com.bearcave.radio17.RadioFragment;

import java.io.IOException;


public abstract class PlayerFragment extends RadioFragment
        implements  View.OnClickListener,
                    Player.OnStateListener{

    public PlayerFragment() {}

    public static final String SOURCE_KEY = "source-key-for-player";

    private String source;
    private Player player;
    private SparseArray<Runnable> buttonMap;

    private ProgressBar loadingBar;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPlayer().stopListeningToAllPlayers();
    }


    protected void setSource(){
        source = getArguments().getString(SOURCE_KEY);
    }

    protected void initialize(){
        player = new Player(this);
        buttonMap = new SparseArray<>();
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
        onPlayChangeIcons();
    }

    protected abstract void onPlayChangeIcons();
    protected abstract void onPauseChangeIcons();

    public boolean isPlaying(){
        return player.isPlaying();
    }

    public void pause(){
        player.pause();
        onPauseChangeIcons();
    }

    protected Player getPlayer(){
        return player;
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
            showLoadingBar();
            player.setAudio(src);
            return true;
        }

        return false;
    }

    public void showLoadingBar(){
        loadingBar.setVisibility(View.VISIBLE);
    }

    public void hideLoadingBar(){
        loadingBar.setVisibility(View.GONE);
    }

    @Override
    public void onPreparedStateListener() {
        hideLoadingBar();
        try {
            player.play();
        } catch (IOException e) {
            notifyAboutInternetConnection();
        }
    }

    @Override
    public void noSourceSetListener() {
        showLoadingBar();
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

    @Override
    public void onCurrentPlayerPlayByAnother() {
        onPlayChangeIcons();
    }

    @Override
    public void onCurrentPlayerPausedByAnother() {
        onPauseChangeIcons();
    }

    @Override
    public void onFinishedStartingService() {}
}
