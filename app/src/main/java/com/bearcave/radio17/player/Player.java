package com.bearcave.radio17.player;


import android.media.AudioManager;
import android.media.MediaPlayer;
import java.io.IOException;
import java.util.Objects;

public final class Player implements MediaPlayer.OnPreparedListener {

    private static String AUDIO_URL = null;
    private final static MediaPlayer mediaPlayer = new MediaPlayer();
    private static boolean needToPrepare = true;

    private Player() {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {  // called after prepareAsync()
        mediaPlayer.start();
    }

    public static void setAudio(String src)  {
        if (Objects.equals(AUDIO_URL, src)){
            return;
        }
        AUDIO_URL = src;
        needToPrepare = true;
    }

    public static void pause(){
        mediaPlayer.pause();
    }

    public static void play() throws IOException {
        if (needToPrepare){
            prepare();
        }
        mediaPlayer.start();
    }

    public static void playPause() throws IOException {
        if(isPlaying()){
            pause();
        } else {
            play();
        }
    }

    private static void prepare() throws IOException {
        needToPrepare = false;
        mediaPlayer.setDataSource(AUDIO_URL);
        mediaPlayer.prepare();
    }

    public static boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }
}
