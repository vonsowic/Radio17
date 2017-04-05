package com.bearcave.radio17.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.io.IOException;


class Player {

    private OnStateListener callback;
    private boolean isBound = false;

    private static Intent playerIntent = null;
    private static PlayerService player;

    Player(PlayerFragment fragment) {
        this.callback = fragment;

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
                        callback.onFinishedStartingService();
                        isBound = true;
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName arg0) {
                    isBound = false;
                }
            };
            fragment.getActivity().bindService(playerIntent, connection, Context.BIND_AUTO_CREATE);

        } else {
            callback.onFinishedStartingService();
        }
    }

    void startListeningToAllPlayers(){
        player.registerListener(callback);
    }

    void stopListeningToAllPlayers() {
        player.unregisterListener(callback);
    }

    interface OnStateListener{
        /**
         * Called when this particular player is not set.
         */
        void onPlayerNotSetListener();

        /**
         * Calls when prepareAsync() has finished
         */
        void onPreparedStateListener();

        /**
         * Called when source from CustomMediaPlayer is null.
         */
        void noSourceSetListener();

        void onCurrentPlayerPlayByAnother();
        void onCurrentPlayerPausedByAnother();
        void onFinishedStartingService();
    }


    void setAudio(String src)  {
        try {
            player.setDJ(callback);
            player.prepare(src);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void seekTo(int point){
       player.seekTo(point);
    }

    int getPosition(){
        return player.getPosition();
    }

    int getDuration(){
        return player.getDuration();
    }

    void pause(){
        player.pause();
    }

    void play() throws IOException {
        if (!isSourceSet()){
            callback.noSourceSetListener();
        }

        if (isThisPlayerSet()) {
            player.play();
        } else {
            callback.onPlayerNotSetListener();
        }
    }

    void forcedPlay() throws IOException {
        player.play();
        callback.onCurrentPlayerPlayByAnother();
    }

    boolean isPlaying(){
        return player.isPlaying();
    }

    boolean isThisPlayerSet(){
        return  player.getCallback() == callback;
    }

    boolean isSourceSet(){
        return player.isSourceSet();
    }

    boolean isCurrentlySet(){
        return isSourceSet() && isThisPlayerSet();
    }
}
