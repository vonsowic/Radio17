package com.bearcave.radio17.list_of_articles.articles

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bearcave.radio17.R
import com.bearcave.radio17.exceptions.NoNewViewException
import com.bearcave.radio17.player.ArticlePlayerFragment
import com.bearcave.radio17.player.PlayerFragment
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * Renders article layout
 * @author Michał Wąsowicz
 */
class LoadArticle(internal val activity: FragmentActivity, internal val context: Context) {

    internal var fontSize: Float = 17F
    internal var playerId = 69

    internal var createNewText = true
    internal var textView: TextView

    internal var root : LinearLayout = LinearLayout(context)
    internal var doc: Document? = null

    internal val options: DisplayImageOptions = DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.logo)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build()

    internal val imageLoader: ImageLoader = ImageLoader.getInstance()

    internal val tagToViewMap = HashMap<String, (Element)->View>()

    // in this list there are tags for which new layout is created
    internal val importantTags = ArrayList<String>(
            Arrays.asList(
                    "div",
                    "p"
            )
    )


    init {
        val config = ImageLoaderConfiguration.Builder(context)
                .build()
        imageLoader.init(config)

        tagToViewMap.put("p", this::addText)
        tagToViewMap.put("b", this::addText)
        tagToViewMap.put("span", this::addText)
        tagToViewMap.put("i", this::addText)
        tagToViewMap.put("em", this::addText)
        tagToViewMap.put("div", this::addDiv)
        tagToViewMap.put("strong", this::addText)

        tagToViewMap.put("img", this::addImage)

        tagToViewMap.put("source", this::addPlayer)

        textView = TextView(context)
    }

    fun prepare(url: String?){
        root = LinearLayout(context)
        root.orientation = LinearLayout.VERTICAL
        doc = Jsoup.connect(url).get()
    }

    fun execute(): View {
        root = LinearLayout(context)
        root.orientation = LinearLayout.VERTICAL

        extract(
                doc?.getElementsByClass("post-content")!!.first()
        )

        return root
    }

    private fun extract(element: Element){
        val elements = element.children()
        add(element)
        for ( child in elements){
            extract(child)
        }
    }

    private fun add(elementView: Element){
        try {
            val view = get(elementView)
            root.addView(view)
        }   catch (e: NoNewViewException){}
            catch (e: NullPointerException){}
    }


    private fun get(element: Element): View {
        val view = tagToViewMap[element.tagName()]?.invoke(element) ?: throw NullPointerException()

        if ( createNewText ) {
            createNewText = false
            return view
        }

        if ( view !is TextView ) {
            createNewText = true
            return view
        }

        throw NoNewViewException()
    }

    private fun addDiv(element: Element): View {
        if (!element.hasClass("gallery")){
            return addText(element)
        } else {
            val galleryView = createGridView()
            val imgs = element.select("img")
            val urls = ArrayList<String>()
            imgs.mapTo(urls) { it.absUrl("src") } // get all urls from img items
            galleryView.adapter = createGalleryAdapter(urls, galleryView)

            element.children().remove() // so in the next get images wont appear in view

            return galleryView
        }
    }

    private fun createGridView(): GridView {
        val view = GridView(context)
        view.layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        )
        view.isScrollContainer = false
        view.numColumns = 3
        view.stretchMode = GridView.STRETCH_COLUMN_WIDTH
        return view
    }

    private fun createGalleryAdapter(urls: ArrayList<String>, view: GridView): BaseAdapter {
        return GalleryAdapter(
                context,
                view,
                urls,
                imageLoader,
                options
        )
    }

    private fun addText(element: Element): View {
        if(createNewText){
            textView = TextView(context)
            textView.textSize = fontSize
        }

        textView.append(Html.fromHtml("<${element.tagName()}>${element.ownText()}</${element.tagName()}>"))
        return textView
    }

    private fun addImage(element: Element): View{
        val iv = ImageView(context)

        Toast.makeText(context, element.parent().parent().parent().parent().toString(), Toast.LENGTH_SHORT).show()
        imageLoader.displayImage(element.absUrl("src"), iv, options)
        return iv
    }

    private fun addPlayer(element: Element): View {
        val player = ArticlePlayerFragment()
        val info = Bundle()
        info.putString(PlayerFragment.SOURCE_KEY, element.attr("src"))
        player.arguments = info

        val layout = FrameLayout(context)
        layout.id = playerId++
        val ft = activity.supportFragmentManager.beginTransaction()
        ft.replace(layout.id, player)
        ft.commit()

        return layout
    }
}

