package com.luisherreralillo.filmica

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class FilmsActivity: AppCompatActivity() {

    val list: RecyclerView by lazy {
        val instance = findViewById<RecyclerView>(R.id.list_films)
        instance.layoutManager = LinearLayoutManager(this)

        instance
    }

    val adapter: FilmsAdapter by lazy {
        val instance = FilmsAdapter { film ->
            this.showDetails(film.id)
        }
        instance.setFilms(FilmsRepo.films)

        instance
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)

        list.adapter = adapter
    }

    fun showDetails(filmId: String) {
        val intentToDetails = Intent(this, DetailsActivity::class.java)
        intentToDetails.putExtra("id", filmId)
        startActivity(intentToDetails)
    }
}