package com.luisherreralillo.filmica.view.films

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.luisherreralillo.filmica.R
import com.luisherreralillo.filmica.data.Film
import com.luisherreralillo.filmica.view.detail.DetailsActivity
import com.luisherreralillo.filmica.view.detail.DetailsFragment
import kotlinx.android.synthetic.main.activity_films.*

class FilmsActivity : AppCompatActivity(), FilmsFragment.onItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)

        if (savedInstanceState == null) {
            val filmsFragment = FilmsFragment()

            // beginTransaction: Conjunto de instruccones para el manejador de fragmentos
            supportFragmentManager.beginTransaction()
                .add(R.id.container_list, filmsFragment)
                .commit()
        }
    }

    override fun onItemClicked(film: Film) {
        showDetails(film.id)
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