package com.bearcave.radio17;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;
    static Intent player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_player) {
            Intent i = null;
            if ( i == null) {
                i = new Intent(this, PlayerActivity.class);
            }
            startActivity(i);
        } else if (id==R.id.nav_recorder) {
          //  startActivity(new Intent(this, RecorderActivity.class));
        } else if (id==R.id.nav_timetable) {
            Intent i = new Intent(this, TimetableActivity.class);
            i.putExtra("article_list_category", getString(R.string.timetable));
            startActivity(i);
        } else if (id==R.id.nav_whatsnewinmusic) {
            Intent i = new Intent(this, ArticleListViewActivity.class);
            i.putExtra("article_list_url", "http://radio17.pl/nowosci-muzyczne/");
            i.putExtra("article_list_category", getString(R.string.whatsnewinmusic));
            startActivity(i);
        } else if (id==R.id.nav_blog) {
            Intent i = new Intent(this, ArticleListViewActivity.class);
            i.putExtra("article_list_url", "http://radio17.pl/category/blogmuzyczny/");
            i.putExtra("article_list_category", getString(R.string.musicblog));
            startActivity(i);
        } else if (id==R.id.nav_news) {
            Intent i = new Intent(this, ArticleListViewActivity.class);
            i.putExtra("article_list_url", "http://radio17.pl/category/aktualnosci/");
            i.putExtra("article_list_category", getString(R.string.news));
            startActivity(i);
        } else if (id==R.id.nav_information) {
            Intent i = new Intent(this, ArticleListViewActivity.class);
            i.putExtra("article_list_url", "http://radio17.pl/category/aktualnosci/info/");
            i.putExtra("article_list_category", getString(R.string.information));
            startActivity(i);
        } else if (id==R.id.nav_podcast) {
            Intent i = new Intent(this, ArticleListViewActivity.class);
            i.putExtra("article_list_url", "http://radio17.pl/category/podcasty/");
            i.putExtra("article_list_category", getString(R.string.podcast));
            startActivity(i);
        } else if (id==R.id.nav_parties) {
            Intent i = new Intent(this, ArticleListViewActivity.class);
            i.putExtra("article_list_url", "http://radio17.pl/category/imprezy/");
            i.putExtra("article_list_category", getString(R.string.parties));
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
