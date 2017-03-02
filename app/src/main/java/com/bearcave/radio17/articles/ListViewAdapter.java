package com.bearcave.radio17.articles;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bearcave.radio17.R;
import com.bearcave.radio17.articles.ArticleActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miwas on 07.09.16.
 */
public class ListViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;

    List<String> articleTitles= new ArrayList<>();
    List<String> articleTexts= new ArrayList<>();
    List<String> imagesUrls= new ArrayList<>();
    List<String> articleUrls= new ArrayList<>();

    DisplayImageOptions options;
    ImageLoader imageLoader;

    public ListViewAdapter(Context context, Document doc) {

        addToLists(doc);
        this.context = context;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.logo) // resource or drawable
                //.showImageForEmptyUri(R.drawable.on_empty_url) // resource or drawable
               // .showImageOnFail(R.drawable.on_fail) // resource or drawable
              //  .resetViewBeforeLoading(true)  // default
                .cacheInMemory(true) // default => false
                .cacheOnDisk(true) // default => false
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.context)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

    }

    @Override
    public int getCount() {
        return articleTitles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {


        TextView title;
        TextView articleText;
        ImageView articlePhoto;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.listview_item, parent, false);

        title = (TextView) itemView.findViewById(R.id.textTitle);
        articleText = (TextView) itemView.findViewById(R.id.textArticle);
        articlePhoto = (ImageView) itemView.findViewById(R.id.imagePoster);

        title.setText(articleTitles.get(position));
        articleText.setText(articleTexts.get(position));


        imageLoader.displayImage(imagesUrls.get(position), articlePhoto, options);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("article_url", articleUrls.get(position));
                intent.putExtra("poster_url", imagesUrls.get(position));
                intent.putExtra("article_title", articleTitles.get(position));
                context.startActivity(intent);

            }
        });



        return itemView;
    }

    public void addToLists(Document doc){


        Elements pom = doc.getElementsByClass("post-title");
        for ( int i = 0; i<pom.size(); i++){
            articleTitles.add(pom.get(i).text());
            articleUrls.add(pom.get(i).select("a").attr("href"));
        }

        pom = doc.getElementsByClass("excerpt-container");
        for ( int i = 0; i<pom.size(); i++){
            articleTexts.add(pom.get(i).text());
        }

        pom = doc.getElementsByClass("gallery-icon");
        for ( int i = 0; i<pom.size(); i++){
            imagesUrls.add(pom.get(i).attr("href"));
        }
    }
}
