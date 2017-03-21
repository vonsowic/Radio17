package com.bearcave.radio17.list_of_articles.articles


import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.bearcave.radio17.R
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 */
class ArticleFragment : Fragment() {

    var callback : OnArticleStateListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as OnArticleStateListener
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater?.inflate(R.layout.content_article, container, false)
        val layout = view?.findViewById(R.id.articleContent) as LinearLayout

        val post = arguments.getSerializable("post-container") as PostContainer
        LoadArticleTask(layout).execute(post.url)

        return view
    }

    inner class LoadArticleTask(val layout: LinearLayout) : AsyncTask<String, Void, LoadArticle>() {

        internal val mProgressDialog: ProgressDialog = ProgressDialog(context)
        internal var noInterentConnection : IOException? = null

        override fun onPreExecute() {
            super.onPreExecute()
            mProgressDialog.setMessage(context.getString(R.string.loading))
            mProgressDialog.isIndeterminate = false
            mProgressDialog.show()
        }

        override fun doInBackground(vararg url: String?): LoadArticle {
            val loader = LoadArticle(context)
            try {
                loader.prepare(url[0])
            } catch (e: IOException){
                noInterentConnection = e
            }
            return loader
        }

        override fun onPostExecute(result: LoadArticle) {
            super.onPostExecute(result)
            mProgressDialog.dismiss()

            if(noInterentConnection != null){
                callback?.onNoInternetConnection()
                return
            }

            layout.addView(result.execute())
            callback?.onArticlePrepared()
        }
    }

    interface OnArticleStateListener{
        fun onNoInternetConnection()
        fun onArticlePrepared()
    }
}
