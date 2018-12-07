package com.luisherreralillo.filmica

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.btn_add)

        button.setOnClickListener {
            Toast.makeText(this, "Added to list", Toast.LENGTH_LONG).show()
        }

    }
}
