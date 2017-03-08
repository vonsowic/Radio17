package com.bearcave.radio17.player;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bearcave.radio17.R;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment implements View.OnClickListener{

    String audioUrl;

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        view.findViewById(R.id.player_play_button).setOnClickListener(this);
        view.findViewById(R.id.player_pause_button).setOnClickListener(this);
        view.findViewById(R.id.player_home_station_button).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_play_button:
                try {
                    Player.play();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.player_pause_button:
                Player.pause();
                break;

            case R.id.player_home_station_button:
                Player.setAudio(getString(R.string.player_url));

                break;
        }
    }
}
