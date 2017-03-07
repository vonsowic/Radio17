package com.bearcave.radio17.list_of_articles;

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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miwas on 07.09.16.
 */
public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;

    private List<String> articleTitles= new ArrayList<>();
    private List<String> articleTexts= new ArrayList<>();
    private List<String> imagesUrls= new ArrayList<>();
    private List<String> articleUrls= new ArrayList<>();

    private DisplayImageOptions options;
    private ImageLoader imageLoader;

    public ListViewAdapter(Context context) {
        this.context = context;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.logo) // resource or drawable
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
                intent.putExtra("article_url",      articleUrls.get(position));
                intent.putExtra("poster_url",       imagesUrls.get(position));
                intent.putExtra("article_title",    articleTitles.get(position));
                context.startActivity(intent);
            }
        });

        return itemView;
    }

    public void addToLists(Document doc){
        Elements posts = doc.getElementById("posts-container").children();
        for (Element post:posts){
            articleTitles.add(post.getElementsByClass("post-title").first().text());
            articleUrls.add(post.getElementsByClass("post-title").first().select("a").attr("href"));
            articleTexts.add(post.getElementsByClass("excerpt-container").first().text());
            imagesUrls.add(post.getElementsByClass("gallery-icon").first().attr("href"));
        }
    }
}
