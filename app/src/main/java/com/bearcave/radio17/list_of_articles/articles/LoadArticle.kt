package com.bearcave.radio17.list_of_articles.articles

import android.content.Context
import android.text.Html
import android.view.View
import android.widget.*

import com.bearcave.radio17.R
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 *
 * @author Michał Wąsowicz
 */
class LoadArticle(internal val context: Context) {

    var fontSize: Float = 17F

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
        tagToViewMap.put("div", this::addText)
        tagToViewMap.put("strong", this::addText)

        tagToViewMap.put("img", this::addImage)
    }

    fun prepare(url: String?){
        root = LinearLayout(context)
        root.orientation = LinearLayout.VERTICAL

        try {
            doc = Jsoup.connect(url).get()

        } catch (e: IOException){
            throw e
        }
    }

    fun execute(): View {
        root = LinearLayout(context)
        root.orientation = LinearLayout.VERTICAL

        extract(
                doc?.getElementsByClass("post-content")!!.first()
        )

        return root
    }

    // TODO: podzielic layout.addView(getView(element)) na dwie czesci: przed wywolaniem rekurencyjnym i po.
    private fun extract(element: Element){
        val elements = element.children()
        add(element)
        for ( child in elements){
            extract(child)
        }
    }

    private fun add(elementView: Element){
        val view = get(elementView)

        if(view != null) {
            root.addView(view)
        }
    }

    private fun get(element: Element): View? {
        return tagToViewMap[element.tagName()]?.invoke(element)
    }

    private fun addText(element: Element): View {
        val tv = TextView(context)
        tv.text = Html.fromHtml("<${element.tagName()}>${element.ownText()}</${element.tagName()}>")
        tv.textSize = fontSize
        return tv
    }

    private fun addImage(element: Element): View{
        val iv = ImageView(context)
        imageLoader.displayImage(element.absUrl("src"), iv, options)
        return iv
    }
}
