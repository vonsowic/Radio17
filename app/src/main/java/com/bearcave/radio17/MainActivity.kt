package com.bearcave.radio17

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
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
import com.bearcave.radio17.list_of_articles.ListViewAdapter
import com.bearcave.radio17.list_of_articles.articles.ArticleFragment
import com.bearcave.radio17.list_of_articles.PostContainer
import com.bearcave.radio17.list_of_articles.articles.TimetableFragment
import com.bearcave.radio17.player.HomePlayerFragment
import com.bearcave.radio17.player.HomeViewFragment
import com.bearcave.radio17.player.PlayerFragment
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.util.*
import kotlin.collections.ArrayList



class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        RadioFragment.NoInternetConnectionListener,
        ArticleFragment.OnArticleStateListener,
        ListViewAdapter.OnArticleCreatedListener,
        HomeViewFragment.Listener {

    object RADIO_STATIC_FIELDS{
        val FRAGMENT_KEY = "fragment_key"
        val POST_CONTAINER = "post-container"
    }

    internal val fragmentMap = SparseArray<RadioFragmentFactory>()
    internal var articleLayout: SlidingUpPanelLayout? = null
    internal val mainPlayerFragment = HomePlayerFragment()

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

        articleLayout = findViewById(R.id.sliding_layout) as SlidingUpPanelLayout?
        articleLayout!!.setOnClickListener { articleLayout?.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED }

        val fab = findViewById(R.id.fab_showing_player) as FloatingActionButton?
        fab!!.setOnClickListener {
            if (articleLayout?.panelState == SlidingUpPanelLayout.PanelState.HIDDEN) {
                setArticlePanelCollapsed()
            } else {
                setArticlePanelHidden()
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

    override fun onStart() {
        super.onStart()
        val info = Bundle()
        info.putString(PlayerFragment.SOURCE_KEY, getString(R.string.player_url))
        mainPlayerFragment.arguments = info
        displayFragment(mainPlayerFragment, R.id.player_placeholder)
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout?
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
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

    private fun displayFragment(fragment: Fragment, layoutId : Int = R.id.content_frame){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(layoutId, fragment)
        ft.commit()
    }

    private fun setArticlePanelCollapsed() {
        articleLayout?.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
    }

    private fun setArticlePanelHidden() {
        articleLayout?.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
    }

    private fun setArticlePanelExpanded() {
        articleLayout?.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
    }

    fun createFragment(factory: RadioFragmentFactory): Fragment {
        val fragment = factory.type.newInstance()
        val info = Bundle()
        info.putStringArrayList(RADIO_STATIC_FIELDS.FRAGMENT_KEY, factory.bundle)
        fragment.arguments = info
        return fragment
    }

    override fun onNoInternetConnectionState() {
        displayFragment(NoInternetConnectionFragment(), R.id.article_placeholder)
        setArticlePanelExpanded()
    }

    override fun onArticlePrepared() {
        articleLayout?.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
    }

    override fun OnArticleLoaded(post: PostContainer) {
        val article = ArticleFragment()
        val info = Bundle()
        info.putSerializable(RADIO_STATIC_FIELDS.POST_CONTAINER, post)
        article.arguments = info

        displayFragment(article, R.id.article_placeholder)
    }

    override fun onMainPlayButtonClickedListener() {
        mainPlayerFragment.onMainButtonClicked()
    }

    inner class RadioFragmentFactory(val type: Class<out Fragment>, val bundle: ArrayList<String>)
}
