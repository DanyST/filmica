package com.luisherreralillo.filmica.view.util

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luisherreralillo.filmica.data.Film

open class BaseFilmAdapter<VH: BaseFilmHolder>( // VH: Clase generica - Las clases por defecto en Kotlin son finales, por lo que hay que añadir 'open' detrás de la clase
    @LayoutRes val layoutItem: Int, // @LayoutRes Java notation para el linter de Android Studio, que nos indique necesita un recurso de tipo layout
    val holderCreator: ((View) -> VH)
): RecyclerView.Adapter<VH>() {

    protected val list: MutableList<Film> = mutableListOf()

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onCreateViewHolder(recyclerView: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater.from(recyclerView.context)
            .inflate(layoutItem, recyclerView, false)
        return holderCreator.invoke(itemView)
    }

    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        val film = list[position]
        viewHolder.bindFilm(film)
    }

    fun setFilms(films: MutableList<Film>) {
        list.clear()
        list.addAll(films)
        notifyDataSetChanged()
    }

    fun getFilm(position: Int): Film {
        return list.get(position)
    }

    fun removeFilmAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addFilmAt(position: Int, film: Film) {
        list.add(position, film)
        notifyItemInserted(position)
    }

    fun addFilms(films: MutableList<Film>, positionStart: Int, itemCount: Int) {
        list.addAll(films)
        notifyItemRangeInserted(positionStart, itemCount)
    }
}