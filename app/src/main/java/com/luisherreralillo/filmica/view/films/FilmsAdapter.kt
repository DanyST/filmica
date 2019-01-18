package com.luisherreralillo.filmica.view.films

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luisherreralillo.filmica.R
import com.luisherreralillo.filmica.data.Film
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

                        Log.d("POSTER URL", value.getPosterUrl())
                        // obtener la instancia de picasso y decirle que cargue la imagen
                        Picasso.get()
                            .load(value.getPosterUrl())
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(imgPoster)
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
    }
}