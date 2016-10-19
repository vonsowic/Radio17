package com.bearcave.radio17;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

        private void enter(int i){
            TextView tv = new TextView(ArticleActivity.this);
            tv.setText("\n");
            tv.setTextSize(font_size);
            for (; i>0; i--) {
                layout.addView(tv);
            }
        }

        private void addText(String text){
            TextView tv = new TextView(ArticleActivity.this, null);
            tv.setText(text+"\n\n");
            tv.setTextSize( font_size );
            layout.addView(tv);
        }

        private void addText(Spanned text) {
            TextView tv = new TextView(ArticleActivity.this, null);
            tv.setText(text);
            tv.setTextSize(font_size);
            layout.addView(tv);
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
                                ImageView ib = new ImageView(ArticleActivity.this);
                                imageLoader.displayImage(elements.get(m).select("a").attr("href"), ib, options);
                                layout.addView(ib);
                                enter();
                            }
                        } else {
                            ImageView ib = new ImageView(ArticleActivity.this);
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

                            final Button button = new Button(ArticleActivity.this);
                            button.setText("PLAY");
                            final String pomUrl = elements.get(i).absUrl("src");
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
}