package com.luisherreralillo.filmica

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

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

    }

}