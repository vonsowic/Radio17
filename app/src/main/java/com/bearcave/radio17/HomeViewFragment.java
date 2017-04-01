package com.bearcave.radio17;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.bearcave.radio17.R;

import org.jsoup.Jsoup;

import java.io.IOException;


public class HomeViewFragment extends Fragment implements View.OnClickListener{

    //TODO:Thread loadSongTitleThread;
    TextView songTitleTextView;
    Listener mainActivity;

    public HomeViewFragment() {}


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (Listener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_view, container, false);
        songTitleTextView = (TextView) view.findViewById(R.id.text_song_title);

        TextView textStation = (TextView) view.findViewById(R.id.stationName);
        textStation.setText(getString(R.string.kanal_glowny));

        ImageButton button = (ImageButton) view.findViewById(R.id.home_button_listen);
        button.setOnClickListener(this);

        getActivity().setTitle(getString(R.string.app_name));

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_button_listen:
                mainActivity.onMainPlayButtonClickedListener();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //TODO:loadSongTitleThread.interrupt();
    }

    public interface Listener{
        void onMainPlayButtonClickedListener();
    }
}
