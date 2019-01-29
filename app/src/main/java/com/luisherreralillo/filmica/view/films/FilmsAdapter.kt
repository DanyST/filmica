package com.luisherreralillo.filmica.view.films

import android.graphics.Bitmap
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luisherreralillo.filmica.R
import com.luisherreralillo.filmica.data.Film
import com.luisherreralillo.filmica.view.util.SimpleTarget
import com.squareup.picasso.Picasso
// Cualquier instancia de view que este dentro de la esta clase, intenta mapearlas a todos los elementos que se encuentras dentro de este archivo
import kotlinx.android.synthetic.main.item_film.view.*

class FilmsAdapter(var itemClickListener: ((Film) -> Unit)? = null) :
    RecyclerView.Adapter<FilmsAdapter.FilmViewHolder>() {

    private val list = mutableListOf<Film>()

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onCreateViewHolder(recyclerView: ViewGroup, type: Int): FilmViewHolder {
        val itemView = LayoutInflater.from(recyclerView.context).inflate(R.layout.item_film, recyclerView, false)

        return FilmViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film: Film = list[position]

        holder.film = film
    }

    fun setFilms(films: MutableList<Film>) {
        list.clear()
        list.addAll(films)
        notifyDataSetChanged()
    }

    inner class FilmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var film: Film? = null
            set(value) {
                field = value

                value?.let {
                    with(itemView) {
                        labelTitle.text = value.title
                        titleGenre.text = value.genre
                        labelVotes.text = value.voteRating.toString()

                        loadImage()
                    }
                }

            }
        init {
            this.itemView.setOnClickListener {
                film?.let {
                    itemClickListener?.invoke(this.film as Film)
                }
            }
        }

        private fun loadImage() {
            /*val target = object : Target {

                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

                            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {

                            }
                        }*/

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


                itemView.container.setBackgroundColor(color) // placeholder color
                itemView.containerData.setBackgroundColor(color)
            }
        }
    }
}