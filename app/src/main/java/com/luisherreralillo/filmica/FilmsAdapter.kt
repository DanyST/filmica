package com.luisherreralillo.filmica

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class FilmsAdapter(var itemClickListener: ((Film) -> Unit)? = null) : RecyclerView.Adapter<FilmsAdapter.FilmViewHolder>() {

    private val list = mutableListOf<Film>()

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onCreateViewHolder(recyclerView: ViewGroup, type: Int): FilmsAdapter.FilmViewHolder {
        val itemView = LayoutInflater.from(recyclerView.context).inflate(R.layout.item_film, recyclerView, false)

        return FilmViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FilmsAdapter.FilmViewHolder, position: Int) {
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
                itemView.findViewById<TextView>(R.id.label_title).text = value?.title
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