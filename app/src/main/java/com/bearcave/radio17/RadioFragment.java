package com.bearcave.radio17;

import android.content.Context;
import android.support.v4.app.Fragment;


import com.bearcave.radio17.player.Player;

import java.io.IOException;

/**
 * Created by miwas on 20.03.17.
 */
public abstract class RadioFragment extends Fragment {

    private NoInternetConnectionListener callback;

    public interface NoInternetConnectionListener{
        void onNoInternetConnectionState();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (NoInternetConnectionListener) context;
        } catch(ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    protected void notifyAboutInternetConnection(){
        callback.onNoInternetConnectionState();
    }
}
