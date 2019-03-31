package com.luisherreralillo.filmica.view.films

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.luisherreralillo.filmica.R
import com.luisherreralillo.filmica.data.Film
import com.luisherreralillo.filmica.view.detail.DetailsActivity
import com.luisherreralillo.filmica.view.detail.DetailsFragment
import com.luisherreralillo.filmica.view.trends.TrendsFragment
import com.luisherreralillo.filmica.view.watchlist.WatchListFragment
import kotlinx.android.synthetic.main.activity_films.*

const val TAG_FILMS = "films"
const val TAG_WATCHLIST = "watchList"
const val TAG_TRENDS = "trends"

class FilmsActivity : AppCompatActivity(), FilmItemClickListener {

    private lateinit var filmsFragment: FilmsFragment
    private lateinit var watchListFragment: WatchListFragment
    private lateinit var trendsFragment: TrendsFragment
    private lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)

        if (savedInstanceState == null) {
            setupFragments()
        } else {
            val activeTag = savedInstanceState.getString("active", TAG_FILMS)
            restoreFragments(activeTag)
        }

        navigation?.setOnNavigationItemSelectedListener { item ->
            val id = item.itemId

            when (id) {
                R.id.action_discover -> showMainFragment(filmsFragment)
                R.id.action_watchlist -> showMainFragment(watchListFragment)
                R.id.action_trends -> showMainFragment(trendsFragment)
            }

            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString("active", activeFragment.tag)
    }

    private fun restoreFragments(activeTag: String) {
        // recuperar las referencias de los fragmentos
        filmsFragment = supportFragmentManager.findFragmentByTag(TAG_FILMS) as FilmsFragment
        watchListFragment = supportFragmentManager.findFragmentByTag(TAG_WATCHLIST) as WatchListFragment
        trendsFragment = supportFragmentManager.findFragmentByTag(TAG_TRENDS) as TrendsFragment

        activeFragment =
                when (activeTag) {
                    TAG_WATCHLIST -> watchListFragment
                    TAG_TRENDS -> trendsFragment
                    else -> filmsFragment
                }

    }

    private fun setupFragments() {
        filmsFragment = FilmsFragment()
        watchListFragment = WatchListFragment()
        trendsFragment = TrendsFragment()
        activeFragment = filmsFragment

        // beginTransaction: Conjunto de instruccones para el manejador de fragmentos
        supportFragmentManager.beginTransaction()
            .add(R.id.container_list, filmsFragment, TAG_FILMS)
            .add(R.id.container_list, watchListFragment, TAG_WATCHLIST)
            .add(R.id.container_list, trendsFragment, TAG_TRENDS)
            .hide(watchListFragment)
            .hide(trendsFragment)
            .commit()
    }

    override fun onItemClicked(film: Film) {
        showDetails(film.id)
    }

    private fun showMainFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .hide(activeFragment)
            .show(fragment)
            .commit()

        activeFragment = fragment
    }

    fun showDetails(id: String) {
        if (isTablet())
            showDetailsFragment(id)
        else
            launchDetailActivity(id)
    }

    private fun isTablet() = this.containerDetails != null

    private fun showDetailsFragment(id: String) {
        val detailsFragment = DetailsFragment.newInstance(id)

        supportFragmentManager.beginTransaction()
            .replace(R.id.containerDetails, detailsFragment)
            .commit()
    }

    private fun launchDetailActivity(id: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

}