package com.bearcave.radio17;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bearcave.radio17.list_of_articles.ArticleListViewFragment;
import com.bearcave.radio17.list_of_articles.TimetableFragment;
import com.bearcave.radio17.player.HomeViewFragment;
import com.bearcave.radio17.player.PlayerFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    HashMap<Integer, Fragment> fragmentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // add fragment player to player placeholder(Sliding layout in main view)
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.player_placeholder, new PlayerFragment());
        ft.commit();

        final SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("HEHLLLLLLLL");
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_showing_player);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN)
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                else
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });

        // initialize map with fragments used in navigation drawer
        fragmentMap.put(R.id.nav_player, new HomeViewFragment());
        fragmentMap.put(R.id.nav_timetable, new TimetableFragment());
        fragmentMap.put(R.id.nav_information, new ArticleListViewFragment());
        fragmentMap.put(R.id.nav_parties, new ArticleListViewFragment());
        fragmentMap.put(R.id.nav_podcast, new ArticleListViewFragment());
        fragmentMap.put(R.id.nav_blog, new ArticleListViewFragment());
        fragmentMap.put(R.id.nav_news, new ArticleListViewFragment());

        // show HomeViewFragment on start
        displaySelectedScreen(R.id.nav_player);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        displaySelectedScreen(item.getItemId());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragmentMap.get(itemId));
        ft.commit();
    }
}
