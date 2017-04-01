package com.bearcave.radio17.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;

import java.io.IOException;


public class Player {

    private OnStateListener callback;
    private boolean isBound = false;

    private static Intent playerIntent = null;
    private static PlayerService player;

    public Player(Fragment fragment) {
        this.callback = (OnStateListener) fragment;

        if (playerIntent == null) {
            playerIntent = new Intent(fragment.getActivity().getBaseContext(), PlayerService.class);
            fragment.getActivity().startService(playerIntent);
            fragment.getActivity().bindService(playerIntent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    public void setAudio(String src)  {
        try {
            player.setDJ(callback);
            player.prepare(src);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAudio()  {
        setAudio(
                callback.onQuestionAboutSourceListener()
        );
    }


    public void pause(){
        player.pause();
    }

    public void play() throws IOException {
        if (isCurrentlySet()) {
            player.play();
        } else {
            callback.noSourceSetListener();
        }
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }

    public void stop() {
        player.stop();
    }

    public interface OnStateListener{
        void onPreparedStateListener();
        void noSourceSetListener();
        String onQuestionAboutSourceListener();
    }

    public boolean isCurrentlySet(){
        return  player.getCallback() == callback &&
                callback.onQuestionAboutSourceListener().equals(player.getDataSource());
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder) service;
            player = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

}
