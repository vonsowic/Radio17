package com.bearcave.radio17.articles

import android.app.Activity
import android.app.ProgressDialog
import android.os.AsyncTask
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.bearcave.radio17.NoInternetConnectionException
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
 * @exception NoInternetConnectionException
 * @author Michał Wąsowicz
 */

class LoadArticle(internal val activity: Activity, internal val layout: LinearLayout) : AsyncTask<String, Void, Element>() {

    internal val mProgressDialog: ProgressDialog = ProgressDialog(activity)
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
        val config = ImageLoaderConfiguration.Builder(activity)
                .build()
        imageLoader.init(config)

        tagToViewMap.put("p", this::addText)
    }

    override fun onPreExecute() {
        super.onPreExecute()
        mProgressDialog.setMessage(activity.getString(R.string.loading))
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
     */
    override fun onPostExecute(result: Element) {
        mProgressDialog.dismiss()
        if (noInternetConnectionException != null) {
            throw NoInternetConnectionException()
        }

        extractElement(result)
    }

    private fun extractElement(element: Element){
        var elements = element.children()

        for ( child in elements){
            layout.addView(getView(child))
        }
    }

    private fun getView(element: Element): View? {
        return tagToViewMap[element.tagName()]?.invoke(element)
    }

    private fun addText(element: Element): View {
        val tv = TextView(activity, null)
        tv.text = Html.fromHtml(element.toString())
        tv.textSize = font_size.toFloat()
        return tv
    }

    private fun addImage(element: Element): View{
        val ib = ImageView(activity)
        imageLoader.displayImage(element.absUrl("src"), ib, options)
        return ib
    }
}