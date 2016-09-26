package com.bearcave.radio17;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.style.TextAlignment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import me.biubiubiu.justifytext.library.JustifyTextView;

public class ArticleActivity extends AppCompatActivity {

    String url=null;
    String poster_url=null;
    DisplayImageOptions options;
    ImageLoader imageLoader;
    static boolean clicked = false;
    static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Intent i = getIntent();
        url = i.getStringExtra("article_url");
        poster_url = i.getStringExtra("poster_url");

        Toolbar toolbar = (Toolbar) findViewById(R.id.articletoolbar);
        toolbar.setTitle(i.getStringExtra("article_title"));
        setSupportActionBar(toolbar);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.logo) // resource or drawable
                //.showImageForEmptyUri(R.drawable.on_empty_url) // resource or drawable
                // .showImageOnFail(R.drawable.on_fail) // resource or drawable
                //  .resetViewBeforeLoading(true)  // default
                .cacheInMemory(true) // default => false
                .cacheOnDisk(true) // default => false
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ArticleActivity.this)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        //TODO: resize articlePoster
        ImageView poster = (ImageView) findViewById(R.id.articlePoster);
        imageLoader.displayImage(poster_url, poster, options);
        new LoadArticle(R.layout.content_article).execute(url);

    }


    private class LoadArticle extends AsyncTask<String, Void, Elements> {

        ProgressDialog mProgressDialog;
        LinearLayout layout;

        public LoadArticle(int content_article) {
            this.layout = (LinearLayout) findViewById(R.id.articleContent);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ArticleActivity.this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();


        }

        @Override
        protected Elements doInBackground(String... urls) {

            Elements postContent = null;
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                try {
                    Document doc = Jsoup.connect(urls[0]).get();
                    postContent = doc.getElementsByClass("post-content").first().children();
                } catch (IOException e) {
                    return null;
                }
            } else {
                // display error
                NoInternetConncetionFragment fragment = new NoInternetConncetionFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.article_activity, fragment);
                fragmentTransaction.commit();
            }


            return postContent;
        }

        @Override
        protected void onPostExecute(Elements result) {

            if (result == null) {
                mProgressDialog.dismiss();
                return;
            }


            Elements elements;
            for (int j = 0; j < result.size(); j++) {

                elements = result.get(j).getAllElements();

                for (int i = 0; i < elements.size(); i++) {

                    String tmp = elements.get(i).tag().toString();

                    // IMAGE
                    if (tmp == "img") {
                        ImageView ib = new ImageView(ArticleActivity.this);
                        imageLoader.displayImage(result.get(j).select("a").attr("href").toString(), ib, options);
                        layout.addView(ib);


                        //AUDIO
                    } else if (tmp == "audio") {
                        final Button button = new Button(ArticleActivity.this);
                        button.setText("PLAY");
                        final String pomUrl = result.get(j).select("a").attr("href");
                        View.OnClickListener buttonListener = new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (clicked) {
                                    stopService(intent);
                                    clicked = false;
                                } else {
                                    intent = new Intent(ArticleActivity.this, PlayerService.class);
                                    intent.putExtra("audio_src", pomUrl);
                                    clicked = true;
                                    startService(intent);
                                }
                            }
                        };
                        layout.addView(button);
                        button.setOnClickListener(buttonListener);


                        //TEXT - strong
                    } else if (tmp == "strong") {
                        TextView tv = new TextView(ArticleActivity.this, null);
                        tv.setText(elements.get(i).text());
                        tv.setTypeface(null, Typeface.BOLD);
                        tv.setTextSize(17);
                        layout.addView(tv);

                        //TEXT - ephazised
                    } else if (tmp == "em") {
                        TextView tv = new TextView(ArticleActivity.this, null);
                        tv.setText(elements.get(i).text());
                        tv.setTypeface(null, Typeface.ITALIC);
                        tv.setTextSize(17);
                        layout.addView(tv);

                        //LINK
                    } else {
                        if (tmp == "a") {
                            //Do nothing

                            //TEXT
                        } else {
                            if (i == 0 && elements.size() > 1) {
                                if (!elements.get(0).text().startsWith(elements.get(1).text())) { //test if the first element contains all
                                    TextView tv = new JustifyTextView(ArticleActivity.this, null);
                                    tv.setText(elements.get(i).text());
                                    tv.setTextSize(17);
                                    layout.addView(tv);

                                }

                            } else {

                            TextView tv = new TextView(ArticleActivity.this);
                            tv.setText(elements.get(i).text());
                            tv.setTextSize(17);
                            layout.addView(tv);



                            }

                        }

                    }
                }
                mProgressDialog.dismiss();
            }
            TextView tv = new TextView(ArticleActivity.this);
            tv.setText("\n \n ");
            tv.setTextSize(17);
            layout.addView(tv);
        }
    }
}
