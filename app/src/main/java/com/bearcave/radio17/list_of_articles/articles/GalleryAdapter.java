package com.bearcave.radio17.list_of_articles.articles;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bearcave.radio17.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by miwas on 26.04.17.
 */

public class GalleryAdapter extends BaseAdapter {

    ArrayList<String> urls;
    ImageLoader loader;
    DisplayImageOptions options;
    LayoutInflater inflater;
    Context context;
    GridView rootView;

    public GalleryAdapter(LayoutInflater inflater, ArrayList<String> urls, ImageLoader loader, DisplayImageOptions options) {
        this.inflater = inflater;
        this.urls = urls;
        this.loader = loader;
        this.options = options;
    }

    public GalleryAdapter(Context context, GridView view, ArrayList<String> urls, ImageLoader loader, DisplayImageOptions options) {
        this(
                ((Activity) context).getLayoutInflater(),
                urls,
                loader,
                options
        );
        this.context = context;
        this.rootView = view;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolder holder;

        if (view == null){
            view = inflater.inflate(R.layout.gallery_item, parent, false);
            holder = new ViewHolder();
            holder.view = ButterKnife.findById(view, R.id.image);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        loader.displayImage(
                urls.get(position),
                holder.view,
                options,
                new ImageLoadingListener() {
                    @Override public void onLoadingStarted(String imageUri, View view) {}
                    @Override public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}
                    @Override public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        rootView.setLayoutParams( new ViewGroup.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        ));
                    }

                    @Override public void onLoadingCancelled(String imageUri, View view) {}
                }
        );


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, urls.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private static class ViewHolder {
        ImageView view;
    }
}
