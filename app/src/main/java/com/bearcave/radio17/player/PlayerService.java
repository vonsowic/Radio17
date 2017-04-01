package com.bearcave.radio17.player;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;

public class PlayerService extends Service{

    private CustomMediaPlayer mediaPlayer;
    private Player.OnStateListener DJ = null;
    private boolean hasStarted = false;

    private final IBinder binder = new PlayerBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer = new CustomMediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                DJ.onPreparedStateListener();
            }
        });
        hasStarted = true;

        return super.onStartCommand(intent, flags, startId);
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void pause(){
        mediaPlayer.pause();
    }

    public void play(){
        mediaPlayer.start();
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public void prepare(String src) throws IOException {
        mediaPlayer.setDataSource(src);
        mediaPlayer.prepareAsync();
    }

    public void setDJ(Player.OnStateListener fragmentPlayer){
        this.DJ = fragmentPlayer;
    }

    public String getDataSource(){
        return mediaPlayer.getDataSource();
    }

    public Player.OnStateListener getCallback(){
        return DJ;
    }

    public class PlayerBinder extends Binder {
        PlayerService getService(){
            return PlayerService.this;
        }
    }
}