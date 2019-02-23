package com.luisherreralillo.filmica.view.detail

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.luisherreralillo.filmica.R
import com.luisherreralillo.filmica.data.Film
import com.luisherreralillo.filmica.data.FilmsRepo
import com.luisherreralillo.filmica.view.util.SimpleTarget
import com.squareup.picasso.Picasso
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

                loadImage(film)
            }

        }

        btnAdd.setOnClickListener {
            film?.let {
                FilmsRepo.saveFilm(context!!, film) {
                    Toast.makeText(this.context, "Added to list", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun loadImage(film: Film) {
        // posee tareas asincronas internas
        val target = SimpleTarget(
            successCallback = { bitmap, from ->
                bitmap?.let {

                    // Añadimos el bitmap a la image view
                    imgPoster.setImageBitmap(bitmap)

                    // Generamos la paleta de colores de la imagen retornada por Picasso
                    setColorFrom(bitmap)

                }
            })

        // strong reference
        // para no ser eliminado por el recolector de basura
        imgPoster.tag = target


        // obtener la instancia de picasso y decirle que cargue la imagen
        Picasso.get()
            .load(film.getPosterUrl())
            .error(R.drawable.placeholder)
            .into(target)
    }

    private fun setColorFrom(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            val defaultColor = ContextCompat.getColor(context!!, R.color.colorPrimary)
            val swatch = palette?.vibrantSwatch ?: palette?.dominantSwatch
            val color = swatch?.rgb ?: defaultColor
            val overlayColor = Color.argb(
                (Color.alpha(color) * 0.5).toInt(),
                Color.red(color),
                Color.green(color),
                Color.blue(color)
            )

            overlay.setBackgroundColor(overlayColor)
            btnAdd.backgroundTintList = ColorStateList.valueOf(color) // Crea una lista de estados con unicamente un color
        }
    }
}