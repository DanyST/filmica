package com.luisherreralillo.filmica.view.util

import android.support.v7.widget.RecyclerView
import android.view.View
import com.luisherreralillo.filmica.data.Film


open class BaseFilmHolder(
    itemView: View,
    itemClickListener: ((Film) -> Unit)? = null
    ): RecyclerView.ViewHolder(itemView) {

    lateinit var film: Film

    init {
        itemView.setOnClickListener {
            itemClickListener?.invoke(film)
        }
    }

    open fun bindFilm(film: Film) {
        this.film = film
    }
}