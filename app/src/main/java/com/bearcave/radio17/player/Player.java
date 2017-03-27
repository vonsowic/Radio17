package com.bearcave.radio17.player;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import java.io.IOException;

public class Player {

    private OnStateListener callback;

    private static PlayerService player = new PlayerService();

    public Player(PlayerFragment fragment) {
        this.callback = fragment;
    }

    public void setAudio(String src)  {
        try {
            player.setDJ(callback);
            player.prepare(src);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause(){
        player.pause();
    }

    public void play() throws IOException {
        //if (isPrepared)
            player.play();
        //else
            callback.noSourceSetListener();
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
    }

    public boolean isCurrentlySet(){
        return player.DJ.equals(callback);
    }

    private static class PlayerService extends Service{

        private MediaPlayer mediaPlayer;
        private OnStateListener DJ;

        private PlayerService(){
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                   DJ.onPreparedStateListener();
                }
            });
            startActivity(new Intent(getBaseContext(), this.getClass()));
        }

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

        private void setDJ(OnStateListener fragmentPlayer){
            this.DJ = fragmentPlayer;
        }
    }
}
