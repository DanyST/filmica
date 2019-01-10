package com.luisherreralillo.filmica.view.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.luisherreralillo.filmica.R

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        if (savedInstanceState == null) {
            val id = intent.getStringExtra("id")

            val detailsFragment = DetailsFragment.newInstance(id)

            // beginTransaction: Conjunto de instruccones para el manejador de fragmentos
            supportFragmentManager.beginTransaction()
                .add(R.id.container_detail, detailsFragment)
                .commit()
        }
    }


}
