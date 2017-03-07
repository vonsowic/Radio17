package com.bearcave.radio17.player;


import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import java.io.IOException;
import java.util.Objects;

public final class PlayerService extends Service implements MediaPlayer.OnPreparedListener {

    private static String AUDIO_URL = null;
    private final static MediaPlayer mediaPlayer = new MediaPlayer();

    private PlayerService() {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            return Service.START_NOT_STICKY;  // TODO: ?????
        }

        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.release();
        super.onDestroy();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {  // called after prepareAsync()
        mediaPlayer.start();
    }

    public static void setAudio(String src)  {
        AUDIO_URL = src;
    }

    public static void pause(){
        mediaPlayer.pause();
    }

    public static void start(){
        mediaPlayer.start();
    }

    public static void playPause() {
        if(isPlaying()){
            pause();
        } else {
            start();
        }
    }

    private static void prepare() throws IOException {
        mediaPlayer.setDataSource(AUDIO_URL);
        mediaPlayer.prepare();
    }

    private static boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }
}
