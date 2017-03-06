package com.bearcave.radio17

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast

import com.bearcave.radio17.articles.LoadArticle
import com.bearcave.radio17.exceptions.NoInternetConnectionException


class TimetableActivity : MainActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = inflater.inflate(R.layout.content_timetable, null, false)
        drawer.addView(contentView, 0)

        var i = intent
        var title = i.getStringExtra("article_list_category")
        var actionBar = supportActionBar
        actionBar!!.title = title

        try {
            LoadArticle(this, (findViewById(R.id.timetableContent) as LinearLayout?)!!).execute("http://radio17.pl/ramowka/")
        } catch (e: NoInternetConnectionException){
            Toast.makeText(this, R.string.no_internet_conn_notification, Toast.LENGTH_LONG).show()
        }
    }
}
