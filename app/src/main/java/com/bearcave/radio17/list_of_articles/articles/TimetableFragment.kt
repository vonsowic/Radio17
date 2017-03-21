package com.bearcave.radio17.list_of_articles.articles


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.bearcave.radio17.R
import com.bearcave.radio17.list_of_articles.articles.LoadArticle
import com.bearcave.radio17.exceptions.NoInternetConnectionException


/**
 * A simple [Fragment] subclass.
 */
class TimetableFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_timetable, container, false)

        try {
            //LoadArticle(context).execute("http://radio17.pl/ramowka/")
        } catch (e: NoInternetConnectionException){
            Toast.makeText(context, R.string.no_internet_connection_notification, Toast.LENGTH_LONG).show()
        }
        return view
    }
}
