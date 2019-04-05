package com.luisherreralillo.filmica.view.watchlist

import android.graphics.Bitmap
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.view.View
import com.luisherreralillo.filmica.R
import com.luisherreralillo.filmica.data.Film
import com.luisherreralillo.filmica.view.util.BaseFilmAdapter
import com.luisherreralillo.filmica.view.util.BaseFilmHolder
import com.luisherreralillo.filmica.view.util.SimpleTarget
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_watchlist.view.*

class WatchlistAdapter(itemClickListener: ((Film) -> Unit)? = null)
    : BaseFilmAdapter<WatchlistAdapter.WatchListHolder>(
    layoutItem = R.layout.item_watchlist,
    holderCreator = { view -> WatchListHolder(view, itemClickListener) }
) {

    class WatchListHolder(itemView: View,
                          listener: ((Film) -> Unit)? = null
    ) : BaseFilmHolder(itemView, listener) {

        override fun bindFilm(film: Film) {
            super.bindFilm(film)

            with(itemView) {
                labelTitle.text = film.title
                labelOverview.text = film.overview
                labelVotes.text = film.voteRating.toString()
                loadImage()
            }

        }

        private fun loadImage() {

            // posee tareas asincronas internas
            val target = SimpleTarget(
                successCallback = { bitmap, from ->
                    bitmap?.let {

                        // Añadimos el bitmap a la image view
                        itemView.imgPoster.setImageBitmap(bitmap)

                        // Generamos la paleta de colores de la imagen retornada por Picasso
                        setColorFrom(bitmap)

                    }
                })

            // strong reference
            // para no ser eliminado por el recolector de basura
            itemView.imgPoster.tag = target

            // obtener la instancia de picasso y decirle que cargue la imagen
            Picasso.get()
                .load(film?.getPosterUrl())
                .error(R.drawable.placeholder)
                .into(target)
        }

        private fun setColorFrom(bitmap: Bitmap) {
            Palette.from(bitmap).generate { palette ->
                val defaultColor = ContextCompat.getColor(itemView.context, R.color.colorPrimary)
                val swatch = palette?.vibrantSwatch ?: palette?.dominantSwatch
                val color = swatch?.rgb ?: defaultColor
                val overlayColor = Color.argb(
                    (Color.alpha(color) * 0.5).toInt(),
                    Color.red(color),
                    Color.green(color),
                    Color.blue(color)
                )

                itemView.imgOverlay.setBackgroundColor(overlayColor)

            }
        }
    }
}