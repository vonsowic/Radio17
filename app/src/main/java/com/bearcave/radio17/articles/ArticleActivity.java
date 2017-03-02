package com.bearcave.radio17.articles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bearcave.radio17.NoInternetConnectionException;
import com.bearcave.radio17.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class ArticleActivity extends AppCompatActivity {

    String url=null;
    String poster_url=null;
    DisplayImageOptions options;
    ImageLoader imageLoader;

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
                .showImageOnLoading(R.drawable.logo)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ArticleActivity.this)
                .build();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        ImageView poster = (ImageView) findViewById(R.id.articlePoster);
        imageLoader.displayImage(poster_url, poster, options);

        try {
            new LoadArticle(ArticleActivity.this, (LinearLayout) findViewById(R.id.articleContent)).execute(url);
        } catch (NoInternetConnectionException e){
            Toast.makeText(this, R.string.no_internet_conn_notification, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}