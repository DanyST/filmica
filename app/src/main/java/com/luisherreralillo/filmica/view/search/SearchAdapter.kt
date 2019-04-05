package com.luisherreralillo.filmica.view.search

import android.view.View
import com.luisherreralillo.filmica.R
import com.luisherreralillo.filmica.data.Film
import com.luisherreralillo.filmica.view.util.BaseFilmAdapter
import com.luisherreralillo.filmica.view.util.BaseFilmHolder
import com.luisherreralillo.filmica.view.util.SimpleTarget
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_search.view.*

class SearchAdapter(itemClickListener: ((Film) -> Unit)? = null):
    BaseFilmAdapter<SearchAdapter.SearchViewHolder>(
        layoutItem = R.layout.item_search,
        holderCreator = { view -> SearchViewHolder(view, itemClickListener) }
    ){

    class SearchViewHolder(view: View,
                           listener: ((Film) -> Unit)? = null
    ): BaseFilmHolder(view, listener) {

        override fun bindFilm(film: Film) {
            super.bindFilm(film)

            with(itemView) {
                labelTitle.text = film.title
                labelGenre.text = film.genre

                loadImage()
            }
        }

        private fun loadImage() {
            val target = SimpleTarget(
                successCallback = { bitmap, from ->
                    bitmap?.let {
                        itemView.imgPoster.setImageBitmap(bitmap)
                    }
                }
            )

            itemView.imgPoster.tag = target

            Picasso.get()
                .load(film?.getPosterUrl())
                .error(R.drawable.placeholder)
                .into(target)
        }
    }

}