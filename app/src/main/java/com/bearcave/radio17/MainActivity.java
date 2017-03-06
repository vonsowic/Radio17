package com.bearcave.radio17;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.bearcave.radio17.list_of_articles.ArticleListViewActivity;
import com.bearcave.radio17.player.PlayerActivity;
import com.bearcave.radio17.player.PlayerFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

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


        final Fragment fragment = new PlayerFragment();
        fragmentTransaction.add(R.id.player_placeholder, fragment);
        fragmentTransaction.hide(fragment);

        fragmentTransaction.commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_player);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                if (fragment.isHidden()) {
                    fragmentTransaction.show(fragment);
                } else {
                    fragmentTransaction.hide(fragment);
                }
                fragmentTransaction.commit();
            }
        });


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
        } else if (id==R.id.nav_timetable) {
            Intent i = new Intent(this, TimetableActivity.class);
            i.putExtra("article_list_category", getString(R.string.timetable));
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
