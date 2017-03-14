package com.bearcave.radio17.articles

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.bearcave.radio17.exceptions.NoInternetConnectionException
import com.bearcave.radio17.R
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException
import java.util.*


/**
 * Loads article from url given from parent activity and displays it on screen.
 * @author Michał Wąsowicz
 */
class LoadArticle(internal val context: Context, internal val layout: LinearLayout) : AsyncTask<String, Void, Element>() {

    internal val mProgressDialog: ProgressDialog = ProgressDialog(context)
    internal val font_size: Int = 17

    internal val options: DisplayImageOptions = DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.logo)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build()
    internal val imageLoader: ImageLoader = ImageLoader.getInstance()
    internal val tagToViewMap = HashMap<String, (Element)->View>()

    private var noInternetConnectionException: IOException? = null


    init {
        val config = ImageLoaderConfiguration.Builder(context)
                .build()
        imageLoader.init(config)

        tagToViewMap.put("p", this::addText)
        tagToViewMap.put("strong", this::addText)
        tagToViewMap.put("b", this::addText)
        tagToViewMap.put("span", this::addText)
        tagToViewMap.put("i", this::addText)
        tagToViewMap.put("em", this::addText)
        tagToViewMap.put("div", this::addText)

        tagToViewMap.put("img", this::addImage)
    }

    override fun onPreExecute() {
        super.onPreExecute()
        mProgressDialog.setMessage(context.getString(R.string.loading))
        mProgressDialog.isIndeterminate = false
        mProgressDialog.show()
    }

    override fun doInBackground(vararg urls: String): Element? {
        try {
            val doc = Jsoup.connect(urls[0]).get()
            return doc.getElementsByClass("post-content").first()
        } catch (e: IOException) {
            noInternetConnectionException = e
        }

        return null
    }

    /**
     * Checks what is in element postContent and shows that on screen
     * @exception NoInternetConnectionException
     */
    override fun onPostExecute(result: Element) {
        mProgressDialog.dismiss()
        if (noInternetConnectionException != null) {
            throw NoInternetConnectionException()
        }

        extractElement(result)
    }

    // TODO: podzielic layout.addView(getView(element)) na dwie czesci: przed wywolaniem rekurencyjnym i po.
    private fun extractElement(element: Element){
        val elements = element.children()
        addView(element)
        for ( child in elements){
            extractElement(child)
        }
    }

    private fun addView(elementView: Element){
        val view = getView(elementView)
        if(view != null) {
            layout.addView(view)
        }
    }

    private fun getView(element: Element): View? {
        return tagToViewMap[element.tagName()]?.invoke(element)
    }

    private fun addText(element: Element): View {
        val tv = TextView(context)
        tv.text = Html.fromHtml("<${element.tagName()}>${element.ownText()}</${element.tagName()}>")
        tv.textSize = font_size.toFloat()
        return tv
    }

    private fun addImage(element: Element): View{
        val ib = ImageView(context)
        imageLoader.displayImage(element.absUrl("src"), ib, options)
        return ib
    }

    private fun addPlayer(element: Element): View{
        val ib = ImageView(context)
        imageLoader.displayImage(element.absUrl("src"), ib, options)
        return ib
    }
}