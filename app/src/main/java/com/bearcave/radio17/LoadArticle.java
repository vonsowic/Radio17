package com.bearcave.radio17;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * Created by miwas on 19.10.16.
 */

public class LoadArticle extends AsyncTask<String, Void, Elements> {

    ProgressDialog mProgressDialog;
    LinearLayout layout;
    Activity activity;
    int font_size;
    DisplayImageOptions options;
    ImageLoader imageLoader;
    static boolean clicked = false;
    static Intent intent;

    private IOException noInternetConnectionException;


    public LoadArticle(Activity activity, LinearLayout layout) {
        this.activity = activity;
        this.layout = layout;
        font_size = 17;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.logo) // resource or drawable
                .cacheInMemory(true) // default => false
                .cacheOnDisk(true) // default => false
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(activity)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }


    private void enter(){
        TextView tv = new TextView(activity);
        tv.setText("\n");
        tv.setTextSize(font_size);
        layout.addView(tv);
    }

    private void addText(String text){
        TextView tv = new TextView(activity, null);
        tv.setText(text+"\n\n");
        tv.setTextSize( font_size );
        layout.addView(tv);
    }

    private void addText(Spanned text) {
        TextView tv = new TextView(activity, null);
        tv.setText(text);
        tv.setTextSize(font_size);
        layout.addView(tv);
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setMessage(activity.getString(R.string.loading));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();

    }

    @Override
    protected Elements doInBackground(String... urls) {

        Elements postContent = null;

        try {
            Document doc = Jsoup.connect(urls[0]).get();
            postContent = doc.getElementsByClass("post-content").first().children();
        } catch (IOException e) {
            noInternetConnectionException = e;
        }

        return postContent;
    }


    //this method checks what is in elements postContent and shows that on screen
    @Override
    protected void onPostExecute(Elements result) {

        if ( noInternetConnectionException != null){
            throw new NoInternetConnectionException();
        }

        boolean onlyText = true;
        mProgressDialog.dismiss();

        if (result == null) {
            return;
        }

        Elements elements;

        for (int j = 0; j < result.size(); j++) {

            elements = result.get(j).getAllElements();

            //removing unnecessery text
            for (int i = 0; i < elements.size(); i++) {
                String tmp = elements.get(i).tag().toString();

                if ( tmp == "img") {
                    onlyText = false;
                    i = elements.size();
                } else if ( tmp == "source"){
                    onlyText = false;
                    i = elements.size();
                    result.get(j).select("a").remove();
                } else if ( tmp =="style"){
                    result.get(j).remove();
                }
            }

            //displaying result on screen
            for (int i = 0; i < elements.size(); i++) {

                String tmp = elements.get(i).tag().toString();

                // IMAGE
                if (tmp == "img") {

                    if ( result.get(j).hasClass("gallery")) {
                        elements = result.get(j).getElementsByClass("gallery-item");

                        for ( int m = 0; m<elements.size(); m++){
                            ImageView ib = new ImageView(activity);
                            imageLoader.displayImage(elements.get(m).select("a").attr("href"), ib, options);
                            layout.addView(ib);
                            enter();
                        }
                    } else {
                        ImageView ib = new ImageView(activity);
                        imageLoader.displayImage(elements.get(i).absUrl("src"), ib, options);
                        layout.addView(ib);
                    }

                    if (result.get(j).hasText() ){
                        addText(result.get(j).text());
                    }

                    i = elements.size(); //  end this elements loop and go to the next result

                    //AUDIO
                } else if (tmp == "source") {

                    if (elements.get(i).attr("type").startsWith("audio")) {

                        final Button button = new Button(activity);
                        button.setText("PLAY");
                        final String pomUrl = elements.get(i).absUrl("src");
                        View.OnClickListener buttonListener = new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (clicked) {
                                    activity.stopService(intent);
                                    clicked = false;
                                } else {
                                    intent = new Intent(activity, PlayerService.class);
                                    intent.putExtra("audio_src", pomUrl);
                                    clicked = true;
                                    activity.startService(intent);
                                }
                            }
                        };
                        layout.addView(button);
                        button.setOnClickListener(buttonListener);

                        if (result.get(j).hasText() ){
                            addText(result.get(j).text());
                        }

                        i = elements.size(); // go to next result's element

                    }
                } else if ( tmp == "style"){}
                //TEXT
                else if ( onlyText ){
                    addText(Html.fromHtml(result.get(j).toString()));
                    i = elements.size();

                }
            }
            onlyText = true;
        }
    }
}
