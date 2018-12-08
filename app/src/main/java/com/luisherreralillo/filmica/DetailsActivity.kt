package com.luisherreralillo.filmica

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val id = intent.getStringExtra("id")
        val film = FilmsRepo.findFilmById(id)

        film?.let {
            with(film) {
                labelTitle.text = title
                labelOverview.text = overview
                labelGenre.text = genre
                labelRelease.text = release
            }

        }

        val button: Button = findViewById(R.id.btn_add)

        button.setOnClickListener {
            Toast.makeText(this, "Added to list", Toast.LENGTH_LONG).show()
        }
    }
}
