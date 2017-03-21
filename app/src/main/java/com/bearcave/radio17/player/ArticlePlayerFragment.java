package com.bearcave.radio17.player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bearcave.radio17.R;

/**
 * Created by miwas on 21.03.17.
 */

public class ArticlePlayerFragment extends PlayerFragment {

    ImageButton playButt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player, container, false);
        player = new Player(this);

        playButt = (ImageButton) view.findViewById(R.id.player_home_station_button);
        playButt.setOnClickListener(this);


        return view;
    }

    @Override
    protected void initialize() {
        super.initialize();
    }
}
