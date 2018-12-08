package com.luisherreralillo.filmica

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button

class FilmsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)

        val button: Button = findViewById(R.id.btn_film)
        button.setOnClickListener {
            Log.d(FilmsActivity::class.java.canonicalName, "Button was clicked")
        }

    }
}