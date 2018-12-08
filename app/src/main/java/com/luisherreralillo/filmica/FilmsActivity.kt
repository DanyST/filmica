package com.luisherreralillo.filmica

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

class FilmsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)

        val list = findViewById<RecyclerView>(R.id.list_films)
        list.layoutManager = LinearLayoutManager(this)

        val adapter = FilmsAdapter()
        list.adapter = adapter
        adapter.setFilms(FilmsRepo.films)
    }

    fun showDetails(clickedView: View) {
        val intentToDetails = Intent(this, DetailsActivity::class.java)
        startActivity(intentToDetails)
    }
}