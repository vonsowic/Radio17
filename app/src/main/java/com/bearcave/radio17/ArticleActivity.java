package com.bearcave.radio17;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
        ImageView poster = (ImageView) findViewById(R.id.articlePoster);
        imageLoader.displayImage(poster_url, poster, options);
        new LoadArticle(ArticleActivity.this, (LinearLayout)findViewById(R.id.articleContent)).execute(url);

    }



}