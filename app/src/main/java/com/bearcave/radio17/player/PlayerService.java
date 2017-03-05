package com.bearcave.radio17.player;


import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;
import java.util.Objects;

public final class PlayerService extends Service {

    private static String AUDIO_URL = null;
    private final static MediaPlayer mediaPlayer = new MediaPlayer();

    private PlayerService() {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
           prepare();
        } catch (IOException e) {
            return Service.START_NOT_STICKY;
        }

        mediaPlayer.start();
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        super.onDestroy();
    }

    public static void setAudio(String src) throws IOException {
        if(Objects.equals(src, AUDIO_URL)){
            return ;
        }

        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        AUDIO_URL = src;
        prepare();
    }

    public static void prepare() throws IOException {
        mediaPlayer.setDataSource(AUDIO_URL);
        mediaPlayer.prepare();
    }

    public static void pause(){
        mediaPlayer.pause();
    }

    public static void start(){
        mediaPlayer.start();
    }

    public static boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }
}
