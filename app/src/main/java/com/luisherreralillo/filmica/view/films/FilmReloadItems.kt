package com.luisherreralillo.filmica.view.films

interface FilmReloadItems {
    fun reloadInitialFilms(page: Int = 1)
    fun reloadNewFilms(page: Int, positionStart: Int)
}