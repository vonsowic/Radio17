package com.bearcave.radio17.list_of_articles


import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
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
import com.bearcave.radio17.MainActivity
import com.bearcave.radio17.RadioFragment


/**
 * Fragment representing list of articles
 */
class ArticleListViewFragment : RadioFragment() {

    internal var listView: ListView? = null
    internal var url = "http://radio17.pl/category/aktualnosci/"
    internal var adapter : ListViewAdapter? = null

    internal var page = 1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_article_list_view, container, false)
        val info = arguments.getStringArrayList(MainActivity.RADIO_STATIC_FIELDS.FRAGMENT_KEY)

        listView = view?.findViewById(R.id.listView) as ListView?

        adapter = ListViewAdapter(
                context,
                activity.supportFragmentManager
        )


        activity.title = info[0]
        url = getString(R.string.radio17_url) + info[1]


        LoadAndPrepareContent().execute(url)

        return view
    }

    private open inner class LoadAndPrepareContent() : AsyncTask<String, Void, Document?>() {

        internal var mProgressDialog = ProgressDialog(context)
        private var noInternetConnectionException: IOException? = null  // or another error
        internal var isLoading = false

        init {

        }


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
                throw NoInternetConnectionException()
            }

            adapter?.addToLists(result)

            if(page>1){
                adapter?.notifyDataSetChanged()
                isLoading = false
            } else {
                listView?.adapter = adapter
            }

            listView?.setOnScrollListener(object : AbsListView.OnScrollListener {

                override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}

                override fun onScroll(view: AbsListView, firstVisibleItem: Int,
                                      visibleItemCount: Int, totalItemCount: Int) {
                    if (firstVisibleItem + visibleItemCount == adapter?.count
                            && !isLoading) {

                        isLoading = true
                        LoadAndPrepareContent().execute(url + "/page/" + ++page)
                    }
                }
            })
        }
    }
}