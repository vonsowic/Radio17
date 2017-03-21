package com.bearcave.radio17.player;


import android.util.SparseArray;
import android.view.View;
import com.bearcave.radio17.RadioFragment;
import java.io.IOException;


public abstract class PlayerFragment extends RadioFragment
        implements  View.OnClickListener,
                    Player.OnStateListener{

    public PlayerFragment() {}

    private String source;
    public static final String SOURCE_KEY = "source-key-for-player";

    protected Player player;
    protected SparseArray<Runnable> buttonMap;

    protected void initialize(){
        player = new Player(this);
        source = getArguments().getString(SOURCE_KEY);
        buttonMap = new SparseArray<>();
    }

    public void onMainButtonClicked(){
        try {
            if (player.isPlaying()) {
                player.pause();
                setDataSource(source);

            } else {
                player.play();
            }
        } catch (IOException e){
            notifyAboutInternetConnection();
        }
    }

    public void playPause(){
        try {
            player.playPause();
        } catch (IOException e) {
            notifyAboutInternetConnection();
        }
    }

    public void play(){
        try {
            player.play();
        } catch (IOException e) {
            notifyAboutInternetConnection();
        }
    }

    public void pause(){
        player.pause();
    }

    public void stop(){
        player.stop();
    }

    @Override
    public void onClick(View v) {
        buttonMap.get(v.getId()).run();
    }

    /**
     * @param source url to audio
     * @return true when source is changed; false otherwise
     */
    public boolean setDataSource(String source){
        if (!source.equals(player.getCurrentlyPlayed())){
            player.setAudio(source);
            return true;
        }

        return false;
    }

    @Override
    public void onPreparedStateListener() {
        try {
            player.play();
        } catch (IOException e) {
            notifyAboutInternetConnection();
        }
    }

    @Override
    public void noSourceSetListener() {
        player.setAudio(source);
    }
}
