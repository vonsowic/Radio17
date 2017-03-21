package com.bearcave.radio17.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;

import java.io.IOException;
import java.util.Objects;

public class Player {

    OnStateListener callback;

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String currentlyPlayed;
    boolean isPrepared = false;

    public Player(PlayerFragment fragment) {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.callback = fragment;

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                isPrepared = true;
                callback.onPreparedStateListener();
            }
        });
    }

    public void setAudio(String src)  {
        try {
            isPrepared = false;
            mediaPlayer.setDataSource(src);
            currentlyPlayed = src;
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause(){
        mediaPlayer.pause();
    }

    public void play() throws IOException {
        if (isPrepared)
            mediaPlayer.start();
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
        return mediaPlayer.isPlaying();
    }

    public boolean isPrepared(){
        return isPrepared;
    }

    public interface OnStateListener{
        void onPreparedStateListener();
        void noSourceSetListener();
    }

    public String getCurrentlyPlayed(){
        return currentlyPlayed;
    }
}
