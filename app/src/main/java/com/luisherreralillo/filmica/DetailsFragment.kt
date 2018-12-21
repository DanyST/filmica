package com.luisherreralillo.filmica

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment: Fragment() {

    companion object {
        fun newInstance(id: String): DetailsFragment {
            val instance = DetailsFragment()
            val args = Bundle()
            args.putString("id", id)
            instance.arguments = args

            return instance
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id: String = arguments?.getString("id") ?: ""

        val film = FilmsRepo.findFilmById(id)

        film?.let {
            with(film) {
                labelTitle.text = title
                labelOverview.text = overview
                labelGenre.text = genre
                labelRelease.text = release
                labelVotes.text = voteRating.toString()
            }

        }

        btnAdd.setOnClickListener {
            Toast.makeText(this.context, "Added to list", Toast.LENGTH_LONG).show()
        }
    }
}