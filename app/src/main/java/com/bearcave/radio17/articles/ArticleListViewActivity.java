package com.bearcave.radio17.articles;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.bearcave.radio17.MainActivity;
import com.bearcave.radio17.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ArticleListViewActivity extends MainActivity {

    ListView listview;
    ListViewAdapter adapter=null;
    String url;
    int page=2;
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_article_list_view, null, false);
        drawer.addView(contentView, 0);

        Intent i = getIntent();
        url = i.getStringExtra("article_list_url");

        //set title sent in MainActivity
        String title = i.getStringExtra("article_list_category");
        ActionBar actionBar = getSupportActionBar();
        try {
            actionBar.setTitle(title);
        } catch (NullPointerException e){
        }


        new LoadAndPrepareContent().execute(url);

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class LoadAndPrepareContent extends AsyncTask<String, Void, Document> {

        ProgressDialog mProgressDialog;
        private IOException noInternetConnectionException;  // or another error

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ArticleListViewActivity.this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Document doInBackground(String... url) {
            Document doc = null;
            try {
                return Jsoup.connect(url[0]).get();
            } catch (IOException e) {
                noInternetConnectionException = e;
            }

            return doc;
        }

        @Override
        protected void onPostExecute(Document result) {

            if ( noInternetConnectionException != null){
                Toast.makeText(
                        ArticleListViewActivity.this,
                        R.string.no_internet_conn_notification,
                        Toast.LENGTH_LONG).show();

                mProgressDialog.dismiss();
                finish();
                return;
            }

            adapter = new ListViewAdapter(ArticleListViewActivity.this, result);

            listview = (ListView) findViewById(R.id.listView);
            listview.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {


                    if ( firstVisibleItem+visibleItemCount== adapter.getCount() &&
                            !isLoading){
                        isLoading = true;
                        new LoadAddContent().execute(url+"page/"+page);
                        page++;
                    }

                }
            });
            listview.setAdapter(adapter);
            mProgressDialog.dismiss();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class LoadAddContent extends LoadAndPrepareContent {

        @Override
        protected void onPostExecute(Document result) {
            if (result == null){
                mProgressDialog.dismiss();
                return;
            }
            adapter.addToLists(result);
            adapter.notifyDataSetChanged();
            mProgressDialog.dismiss();
            isLoading = false;
        }
    }
}



