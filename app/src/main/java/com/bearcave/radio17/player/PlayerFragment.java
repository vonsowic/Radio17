package com.bearcave.radio17.player;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bearcave.radio17.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment implements View.OnClickListener{


    public PlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        //Button button = (Button) view.findViewById(R.id.button2);
        //button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
       /* switch (v.getId()) {
            case R.id.button4:
                PlayerService.pause();
                break;
        }*/
    }
}
