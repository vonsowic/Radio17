package com.bearcave.radio17.list_of_articles;

import android.content.Context;
import android.support.v4.app.FragmentManager;

/**
 * Created by miwas on 21.03.17.
 */

public class SearchListViewAdapter extends ListViewAdapter {
    public SearchListViewAdapter(Context context, FragmentManager fragmentManager) {
        super(context, fragmentManager);
    }

    /*
    @Override
    protected String getPostContainerId() {
        return "post-wraper";
    }
    */

    @Override
    protected String getPostTitleClassName() {
        return "entry-title";
    }
}
