package com.luisherreralillo.filmica

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class FilmsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)
    }

    fun showDetails(clickedView: View) {
        val intentToDetails: Intent = Intent(this, DetailsActivity::class.java)
        startActivity(intentToDetails)
    }
}