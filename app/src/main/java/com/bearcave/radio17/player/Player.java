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
            ServiceConnection connection = new ServiceConnection() {

                @Override
                public void onServiceConnected(ComponentName className,
                                               IBinder service) {
                    if (!isBound) {
                        PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder) service;
                        player = binder.getService();
                        isBound = true;
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName arg0) {
                    isBound = false;
                }
            };
            fragment.getActivity().bindService(playerIntent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    public interface OnStateListener{
        /**
         * Called when this particular player is not set.
         */
        void onPlayerNotSetListener();

        /**
         *
         */
        void onPreparedStateListener();

        /**
         * Called when source from CustomMediaPlayer is null.
         */
        void noSourceSetListener();

        /**
         * @return source from player Fragment.
         */
        String onQuestionAboutSourceListener();
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

    public void seekTo(int point){
       player.seekTo(point);
    }

    public int getPosition(){
        return player.getPosition();
    }

    public int getDuration(){
        return player.getDuration();
    }

    public void pause(){
        player.pause();
    }

    public void play() throws IOException {
        if (!isSourceSet()){
            callback.noSourceSetListener();
        }

        if (isThisPlayerSet()) {
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

    public boolean isThisPlayerSet(){
        return  player.getCallback() == callback;
    }

    public boolean isSourceSet(){
        return player.isSourceSet();
    }

    public boolean isCurrentlySet(){
        return isSourceSet() && isThisPlayerSet();
    }
}
