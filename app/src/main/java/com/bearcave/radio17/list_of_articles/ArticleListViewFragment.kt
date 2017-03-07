package com.bearcave.radio17.list_of_articles


import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ListView
import android.widget.Toast

import com.bearcave.radio17.R
import com.bearcave.radio17.exceptions.NoInternetConnectionException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 */
class ArticleListViewFragment : Fragment() {

    internal var listView: ListView? = null
    internal val url = "http://radio17.pl/category/aktualnosci/"
    internal var page = 1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_article_list_view, container, false)
        listView = view?.findViewById(R.id.listView) as ListView?

        LoadAndPrepareContent().execute(url)
        return view
    }

    private open inner class LoadAndPrepareContent : AsyncTask<String, Void, Document?>() {

        internal var mProgressDialog = ProgressDialog(context)
        private var noInternetConnectionException: IOException? = null  // or another error
        internal val adapter = ListViewAdapter(context)


        override fun onPreExecute() {
            super.onPreExecute()
            mProgressDialog.setMessage(getString(R.string.loading))
            mProgressDialog.isIndeterminate = false
            mProgressDialog.show()
        }

        override fun doInBackground(vararg url: String): Document? {
            val doc: Document? = null
            try {
                return Jsoup.connect(url[0]).get()
            } catch (e: IOException) {
                noInternetConnectionException = e
            }

            return doc
        }

        override fun onPostExecute(result: Document?) {
            mProgressDialog.dismiss()

            if (noInternetConnectionException != null) {
                Toast.makeText(
                        context,
                        R.string.no_internet_conn_notification,
                        Toast.LENGTH_LONG).show()

                activity.finish()
                return
            }

            adapter.addToLists(result)
            adapter.notifyDataSetChanged()

            listView?.setOnScrollListener(object : AbsListView.OnScrollListener {

                internal var isLoading = false

                override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {

                }

                override fun onScroll(view: AbsListView, firstVisibleItem: Int,
                                      visibleItemCount: Int, totalItemCount: Int) {
                    if (firstVisibleItem + visibleItemCount == adapter.count
                            && !isLoading) {

                        isLoading = true
                        LoadAndPrepareContent().execute(url + "page/" + ++page)
                    }
                }
            })

            listView?.adapter = adapter
        }
    }
}