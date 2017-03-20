package com.bearcave.radio17.list_of_articles;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bearcave.radio17.R;
import com.bearcave.radio17.list_of_articles.articles.ArticleFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michał Wąsowicz
 */
public class ListViewAdapter extends BaseAdapter {

    private Context context;

    private List<String> articleTitles  =   new ArrayList<>();
    private List<String> articleTexts   =   new ArrayList<>();
    private List<String> imagesUrls     =   new ArrayList<>();
    private List<String> articleUrls    =   new ArrayList<>();

    private DisplayImageOptions options;
    private ImageLoader imageLoader;

    private FragmentManager fragmentManager;

    public ListViewAdapter(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;

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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.listview_item, parent, false);

        TextView title = (TextView) itemView.findViewById(R.id.textTitle);
        title.setText(articleTitles.get(position));

        TextView articleText = (TextView) itemView.findViewById(R.id.textArticle);
        articleText.setText(articleTexts.get(position));

        ImageView articlePhoto = (ImageView) itemView.findViewById(R.id.imagePoster);
        imageLoader.displayImage(imagesUrls.get(position), articlePhoto, options);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Fragment articleFragment = new ArticleFragment();
                Bundle info = new Bundle();
                info.putString("article_url", articleUrls.get(position));
                articleFragment.setArguments(info);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.article_placeholder, articleFragment);
                fragmentTransaction.commit();

            }
        });

        return itemView;
    }

    protected String getPostContainerId() {
        return "posts-container";
    }

    protected String getPostTitleClassName(){
        return "post-title";
    }

    public void addToLists(Document doc){
        Elements posts = doc.getElementById(getPostContainerId()).children();
        for (Element post:posts){
            articleTitles.add(post.getElementsByClass(getPostTitleClassName()).first().text());
            articleUrls.add(post.getElementsByClass(getPostTitleClassName()).first().select("a").attr("href"));
            articleTexts.add(post.getElementsByClass("excerpt-container").first().text());
            imagesUrls.add(post.getElementsByClass("gallery-icon").first().attr("href"));
        }
    }
}
