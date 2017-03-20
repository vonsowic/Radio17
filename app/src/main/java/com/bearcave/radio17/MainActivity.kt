package com.bearcave.radio17

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.SparseArray
import android.view.Menu
import android.view.MenuItem
import com.bearcave.radio17.exceptions.NoInternetConnectionFragment
import com.bearcave.radio17.list_of_articles.ArticleListViewFragment
import com.bearcave.radio17.list_of_articles.articles.TimetableFragment
import com.bearcave.radio17.player.HomeViewFragment
import com.bearcave.radio17.player.PlayerFragment
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.util.*
import kotlin.collections.ArrayList



class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        RadioFragment.NoInternetConnectionListener {



    object RADIO_STATIC_FIELDS{
        val FRAGMENT_KEY = "fragment_key"
    }

    internal val fragmentMap = SparseArray<RadioFragmentFactory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        setSupportActionBar(toolbar)

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout?
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView?
        navigationView!!.setNavigationItemSelectedListener(this)

        // add fragment player to player placeholder(Sliding layout in main view)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.player_placeholder, PlayerFragment())
        ft.commit()


        val slidingUpPanelLayout = findViewById(R.id.sliding_layout) as SlidingUpPanelLayout?
        slidingUpPanelLayout!!.setOnClickListener { slidingUpPanelLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED }


        val fab = findViewById(R.id.fab_showing_player) as FloatingActionButton?
        fab!!.setOnClickListener {
            if (slidingUpPanelLayout.panelState == SlidingUpPanelLayout.PanelState.HIDDEN) {
                slidingUpPanelLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                fab.setImageResource(android.R.drawable.arrow_down_float)
            } else {
                slidingUpPanelLayout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
                fab.setImageResource(android.R.drawable.arrow_up_float)
            }
        }

        // initialize map with fragments used in navigation drawer
        fragmentMap.put(R.id.nav_player,        RadioFragmentFactory(HomeViewFragment::class.java,          ArrayList<String>()))
        fragmentMap.put(R.id.nav_timetable,     RadioFragmentFactory(TimetableFragment::class.java,         ArrayList(Arrays.asList(getString(R.string.timetable), "/ramowka"))))
        fragmentMap.put(R.id.nav_news,          RadioFragmentFactory(ArticleListViewFragment::class.java,   ArrayList(Arrays.asList(getString(R.string.news), "/category/aktualnosci"))))
        fragmentMap.put(R.id.nav_musicblog,     RadioFragmentFactory(ArticleListViewFragment::class.java,   ArrayList(Arrays.asList(getString(R.string.parties), "/category/blogmuzyczny"))))
        fragmentMap.put(R.id.nav_interview,     RadioFragmentFactory(ArticleListViewFragment::class.java,   ArrayList(Arrays.asList(getString(R.string.podcast), "/category/blogmuzyczny/wywiady"))))
        fragmentMap.put(R.id.nav_concerts,      RadioFragmentFactory(ArticleListViewFragment::class.java,   ArrayList(Arrays.asList(getString(R.string.concerts), "/category/blogmuzyczny/zespoly"))))
        fragmentMap.put(R.id.nav_reports,       RadioFragmentFactory(ArticleListViewFragment::class.java,   ArrayList(Arrays.asList(getString(R.string.reports), "/category/blogmuzyczny/relacje"))))
        fragmentMap.put(R.id.nav_reviews,       RadioFragmentFactory(ArticleListViewFragment::class.java,   ArrayList(Arrays.asList(getString(R.string.reviews), "/category/blogmuzyczny/recenzje"))))

        // show HomeViewFragment on start
        displaySelectedScreen(R.id.nav_player)
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout?
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchView = MenuItemCompat.getActionView(menu.findItem(R.id.action_search)) as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        //searchView.setIconifiedByDefault(false)

        val queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                // this is your adapter that will be filtered
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                //Here u can get the value "query" which is entered in the search box.
                supportFragmentManager
                        .beginTransaction()
                        .replace(
                                R.id.content_frame,
                                createFragment(
                                        RadioFragmentFactory(
                                                ArticleListViewFragment::class.java,
                                                ArrayList(
                                                        Arrays.asList(
                                                                getString(R.string.app_name),
                                                                "/?s="+query)
                                                )
                                        )
                                )
                        )
                        .commit()
                return true
            }
        }
        searchView.setOnQueryTextListener(queryTextListener)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        displaySelectedScreen(item.itemId)
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout?
        drawer!!.closeDrawer(GravityCompat.START)
        return true
    }

    private fun displaySelectedScreen(itemId: Int) {
        displayFragment(createFragment(fragmentMap[itemId]))
    }

    private fun displayFragment(fragment: Fragment){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.content_frame, fragment)
        ft.commit()
    }

    fun createFragment(factory: RadioFragmentFactory): Fragment {
        val fragment = factory.type.newInstance()
        val info = Bundle()
        info.putStringArrayList(RADIO_STATIC_FIELDS.FRAGMENT_KEY, factory.bundle)
        fragment.arguments = info
        return fragment
    }

    override fun noInternetConnection() {
        displayFragment(NoInternetConnectionFragment())
    }

    inner class RadioFragmentFactory(val type: Class<out Fragment>, val bundle: ArrayList<String>)
}
