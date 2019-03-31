package com.luisherreralillo.filmica.view.films

import com.luisherreralillo.filmica.data.Film

interface FilmItemClickListener {
    fun onItemClicked(film: Film)
}