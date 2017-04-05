package com.bearcave.radio17.player;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by miwas on 28.03.17.
 */

public class CustomMediaPlayer extends MediaPlayer {

    private String source = null;

    public CustomMediaPlayer() {
        super();
    }

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        super.setDataSource(path);
        source = path;
    }

    public boolean isSourceSet(){
        return source != null;
    }

    public String getDataSource(){
        return source;
    }
}
