package com.bearcave.radio17.player;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.bearcave.radio17.R;


public class ArticlePlayerFragment extends PlayerFragment {

    private ImageButton playButt;
    private SeekBar seekBar;

    private static final Integer DELAY = 300;
    private Handler timerHandler = new Handler();

    Runnable timer = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(
                    getPlayer().getPosition()
            );

            timerHandler.postDelayed(this, DELAY);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_player, container, false);

        setSource();

        playButt = (ImageButton) view.findViewById(R.id.article_play_button);
        playButt.setOnClickListener(this);

        seekBar = (SeekBar) view.findViewById(R.id.article_player_seekbar);
        seekBar.setOnSeekBarChangeListener(new OnArticleSeekBarChangeListener());

        initialize();
        return view;
    }

    @Override
    protected void initialize() {
        super.initialize();
        put(R.id.article_play_button, new OnPlayButtonClicked());
    }

    @Override
    protected void onPlayChangeIcons() {
        playButt.setImageResource(R.drawable.ic_pause_black_24dp);
    }

    @Override
    protected void onPauseChangeIcons() {
        playButt.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    }

    @Override
    public void play() {
        super.play();
        startTimer();
    }

    private void startTimer(){
        timerHandler.postDelayed(timer, DELAY);
    }

    @Override
    public void pause() {
        super.pause();
        stopTimer();
    }

    private void stopTimer() {
        timerHandler.removeCallbacks(timer);
    }

    @Override
    public boolean setDataSource(String src) {
        return super.setDataSource(src);
    }

    @Override
    public void onPlayerNotSetListener() {
        noSourceSetListener();
    }

    @Override
    public void onPreparedStateListener() {
        super.onPreparedStateListener();
        seekBar.setMax(getPlayer().getDuration());
    }

    @Override
    public void onCurrentPlayerPlayByAnother() {
        play();
    }

    @Override
    public void onCurrentPlayerPausedByAnother() {
        pause();
    }

    private class OnPlayButtonClicked implements Runnable{

        @Override
        public void run() {
            if (getPlayer().isThisPlayerSet()){
                playPause();
            } else {
                play();
            }
        }
    }

    private class OnArticleSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (getPlayer().isThisPlayerSet()) {
                getPlayer().seekTo(
                        seekBar.getProgress()
                );
            }
        }
    }
}
