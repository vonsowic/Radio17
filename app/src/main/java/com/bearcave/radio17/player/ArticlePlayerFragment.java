package com.bearcave.radio17.player;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.bearcave.radio17.R;

/**
 * Created by miwas on 21.03.17.
 */

public class ArticlePlayerFragment extends PlayerFragment {

    ImageButton playButt;
    SeekBar seekBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_player, container, false);

        setSource();

        playButt = (ImageButton) view.findViewById(R.id.article_play_button);
        playButt.setOnClickListener(this);

        seekBar = (SeekBar) view.findViewById(R.id.article_player_seekbar);

        initialize();
        return view;
    }

    @Override
    protected void initialize() {
        super.initialize();
        put(R.id.article_play_button, new OnPlayButtonClicked());
    }

    @Override
    public void play() {
        super.play();
        playButt.setImageResource(R.drawable.ic_pause_black_24dp);
    }

    @Override
    public void stop() {
        super.stop();
        playButt.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    }

    private class OnPlayButtonClicked implements Runnable{

        @Override
        public void run() {
            playPause();
        }
    }
}
