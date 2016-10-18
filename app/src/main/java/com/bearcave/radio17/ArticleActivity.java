package com.bearcave.radio17;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    final int font_size = 17;
    final String strong = "strong" ;
    final String em = "em";

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


        private void enter(){
            TextView tv = new TextView(ArticleActivity.this);
            tv.setText("\n");
            tv.setTextSize(font_size);
            layout.addView(tv);
        }

        private void addText(String text){
            TextView tv = new TextView(ArticleActivity.this, null);
            tv.setText(Html.fromHtml(text));
            tv.setTextSize( font_size );
            layout.addView(tv);
           // enter();

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


        //this method checks what is in elements postContent and shows that on screen
        @Override
        protected void onPostExecute(Elements result) {

            mProgressDialog.dismiss();

            if (result == null) {
                return;
            }

            
            
        }
    }


}
