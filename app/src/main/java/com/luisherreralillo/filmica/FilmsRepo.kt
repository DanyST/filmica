package com.luisherreralillo.filmica

// Se mantendrá en memoria como unica instancia Singleton
object FilmsRepo {
    val films: MutableList<Film> = mutableListOf()
        get() {
            // WildCard field accede a la instancia de films y no al get
            if (field.isEmpty()) {
                field.addAll(dummyFilms())
            }

            return field
        }

    fun findFilmById(id: String): Film? {
        return films.find { it.id == id }
    }

    private fun dummyFilms(): List<Film> {
        return (0..9).map {
           Film(
                title = "Film $it",
                genre = "$it",
                release = "200$it-0$it-0$it",
                voteRating = it.toDouble(),
                overview = "Overview $it"
            )
        }
    }
}