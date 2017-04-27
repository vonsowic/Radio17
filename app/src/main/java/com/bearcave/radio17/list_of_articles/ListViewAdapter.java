package com.bearcave.radio17.list_of_articles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bearcave.radio17.R;
import com.bearcave.radio17.list_of_articles.articles.PostContainer;
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
    private List<PostContainer> posts  =   new ArrayList<>();
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private OnArticleCreatedListener callback;


    public ListViewAdapter(Context context) {
        this.context = context;
        callback = (OnArticleCreatedListener) context;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.logo) // resource or drawable
                .cacheInMemory(true)
                .cacheOnDisk(true) // default => false
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.context)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.listview_item, parent, false);

        TextView title = (TextView) itemView.findViewById(R.id.textTitle);
        title.setText(posts.get(position).getTitle());

        TextView articleText = (TextView) itemView.findViewById(R.id.textArticle);
        articleText.setText(posts.get(position).getText());

        ImageView articlePhoto = (ImageView) itemView.findViewById(R.id.imagePoster);
        imageLoader.displayImage(posts.get(position).getImageUrl(), articlePhoto, options);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                callback.OnArticleLoaded(posts.get(position));
            }
        });

        return itemView;
    }


    public void addToLists(Document doc){
        Elements postsElements = doc.getElementById("posts-container").children();
        for (Element post:postsElements){
            posts.add(
                    new PostContainer(
                            post.getElementsByClass("post-title").first().select("a").attr("href"),
                            post.getElementsByClass("post-title").first().text(),
                            post.getElementsByClass("gallery-icon").first().attr("href"),
                            post.getElementsByClass("excerpt-container").first().text()
                            )
            );
        }
    }

    public interface OnArticleCreatedListener{
        void OnArticleLoaded(PostContainer postContainer);
    }
}
