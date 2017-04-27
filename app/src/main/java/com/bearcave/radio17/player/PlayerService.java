package com.bearcave.radio17.player;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashSet;

public class PlayerService extends Service{

    private CustomMediaPlayer mediaPlayer;
    private Player.OnStateListener DJ = null;

    private final IBinder binder = new PlayerBinder();
    private final HashSet<Player.OnStateListener> listeners = new HashSet<>();

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

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    public void pause(){
        mediaPlayer.pause();
        for (Player.OnStateListener listener : listeners){
            listener.onCurrentPlayerPausedByAnother();
        }

        if (DJ != null){
            DJ.onCurrentPlayerPausedByAnother();
        }
    }

    public void play(){
        mediaPlayer.start();

        for (Player.OnStateListener listener : listeners){
            listener.onCurrentPlayerPlayByAnother();
        }

        if (DJ != null){
            DJ.onCurrentPlayerPlayByAnother();
        }
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public void prepare(String src) throws IOException {
        mediaPlayer.reset();
        mediaPlayer.setDataSource(src);
        mediaPlayer.prepareAsync();
    }

    public void setDJ(Player.OnStateListener fragmentPlayer){
        for (Player.OnStateListener listener : listeners){
            listener.onCurrentPlayerPausedByAnother();
        }

        if (DJ != null){
            DJ.onCurrentPlayerPausedByAnother();
        }

        this.DJ = fragmentPlayer;
    }

    public String getDataSource(){
        return mediaPlayer.getDataSource();
    }

    public Player.OnStateListener getCallback(){
        return DJ;
    }

    public boolean isSourceSet(){
        return mediaPlayer.isSourceSet();
    }

    public void seekTo(int point){
        mediaPlayer.seekTo(point);
    }

    public int getPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    public void registerListener(Player.OnStateListener listener){
        listeners.add(listener);
    }

    public void unregisterListener(Player.OnStateListener listener){
        listeners.remove(listener);

        if ( DJ == listener){
            DJ = null;
        }
    }

    class PlayerBinder extends Binder {
        PlayerService getService(){
            return PlayerService.this;
        }
    }
}
