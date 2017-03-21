package com.bearcave.radio17.player;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.io.IOException;
import java.util.Objects;

public class Player {

    private OnStateListener callback;

    private static PlayerService player;
    private static String currentlyPlayed;
    private boolean isPrepared = false;

    public Player(PlayerFragment fragment) {
        this.callback = fragment;
    }

    public void setAudio(String src)  {
        try {
            isPrepared = false;
            currentlyPlayed = src;
            player.prepare(src);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause(){
        player.pause();
    }

    public void play() throws IOException {
        if (isPrepared)
            player.play();
        else
            callback.noSourceSetListener();
    }

    public void playPause() throws IOException {
        if(isPlaying()){
            pause();
        } else {
            play();
        }
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }

    public boolean isPrepared(){
        return isPrepared;
    }

    public void stop() {
        player.stop();
    }

    public interface OnStateListener{
        void onPreparedStateListener();
        void noSourceSetListener();
    }

    public String getCurrentlyPlayed(){
        return currentlyPlayed;
    }

    private class PlayerService extends Service{

        private MediaPlayer mediaPlayer = new MediaPlayer();

        PlayerService(){
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    isPrepared = true;
                    callback.onPreparedStateListener();
                }
            });
        }


        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

            return super.onStartCommand(intent, flags, startId);
        }

        public void stop() {
            mediaPlayer.stop();
        }

        public void pause(){
            mediaPlayer.pause();
        }

        public boolean isPlaying(){
            return mediaPlayer.isPlaying();
        }

        public void play() {
            mediaPlayer.start();
        }

        public void prepare(String src) throws IOException {
            mediaPlayer.setDataSource(src);
            mediaPlayer.prepareAsync();
        }
    }
}
