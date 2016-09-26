package com.bearcave.radio17;


import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

public class PlayerService extends Service {

    private String AUDIO_URL = "http://tolo.me:8000/;";
    private MediaPlayer mediaPlayer;

    public PlayerService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.

        if (intent != null) {
            AUDIO_URL = intent.getStringExtra("audio_src");
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(AUDIO_URL);
            mediaPlayer.prepare();
        } catch (IOException e) {
            return 0;
        }

        mediaPlayer.start();
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        super.onDestroy();
    }


}
