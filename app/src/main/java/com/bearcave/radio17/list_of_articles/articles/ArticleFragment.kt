package com.bearcave.radio17.list_of_articles.articles


import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.bearcave.radio17.R


/**
 * A simple [Fragment] subclass.
 */
class ArticleFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater?.inflate(R.layout.content_article, container, false)
        val layout = view?.findViewById(R.id.articleContent) as LinearLayout
        val url = arguments.getString("article_url")

        LoadArticleTask(layout).execute(url)


        return view
    }

    inner class LoadArticleTask(val layout: LinearLayout) : AsyncTask<String, Void, LoadArticle>() {

        internal val mProgressDialog: ProgressDialog = ProgressDialog(context)

        override fun onPreExecute() {
            super.onPreExecute()
            mProgressDialog.setMessage(context.getString(R.string.loading))
            mProgressDialog.isIndeterminate = false
            mProgressDialog.show()
        }

        override fun doInBackground(vararg url: String?): LoadArticle {
            val loader = LoadArticle(context)
            loader.prepare(url[0])
            return loader
        }

        override fun onPostExecute(result: LoadArticle) {
            super.onPostExecute(result)
            mProgressDialog.dismiss()
            layout.addView(result.execute())
        }
    }
}
