package com.luisherreralillo.filmica

import android.content.Context
import android.net.Uri
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

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
                genre = "Genre $it",
                release = "200$it-0$it-0$it",
                voteRating = it.toDouble(),
                overview = "Overview $it"
            )
        }
    }

    fun discoverFilms(context: Context) {
        // JsonObjectRequest: instancia de una petición
        // obtiene toda la informacion de lo que va a realizar la peticion
        val url = discoverUrl()
        val request = JsonObjectRequest(Request.Method.GET, url, null, {
            val films = parseFilms(it)
        }, { error ->
            error.printStackTrace()
        })

        Volley.newRequestQueue(context)
            .add(request)
    }

    private fun parseFilms(response: JSONObject): MutableList<Film> {
        val films = mutableListOf<Film>()
        val filmsArray = response.getJSONArray("results")

        for (i in 0 ..(filmsArray.length() - 1)) {
            val film = parseFilm(filmsArray.getJSONObject(i))
            films.add(film)
        }

        return films
    }

    private fun parseFilm(jsonFilm: JSONObject): Film {
        return Film(
            id = jsonFilm.getInt("id").toString(),
            title = jsonFilm.getString("title"),
            overview = jsonFilm.getString("overview"),
            voteRating = jsonFilm.getDouble("vote_average"),
            release = jsonFilm.getString("release_date"),
            genre = parseGenres(jsonFilm.getJSONArray("genre_ids"))
        )
    }

    private fun parseGenres(genresArray: JSONArray): String {
        val genres = mutableListOf<String>()

        for (i in 0..(genresArray.length() -1)) {
            val genreId = genresArray.getInt(i)
            val genre = ApiConstants.genres[genreId] ?: ""
            genres.add(genre)
        }

        return genres.reduce { acc, genre -> "$acc | $genre" }
    }

    private fun discoverUrl(language: String = "en-US", sortBy: String = "popularity.desc", page: Int = 1): String {
        return Uri.Builder()
            .scheme("https")
            .authority("api.themoviedb.org")
            .appendPath("3")
            .appendPath("discover")
            .appendPath("movie")
            .appendQueryParameter("api_key", "f3d7b2c0ace82b34d96ee91121c22443")
            .appendQueryParameter("language", language)
            .appendQueryParameter("sort_by", sortBy)
            .appendQueryParameter("page", page.toString())
            .appendQueryParameter("include_adult", "false")
            .appendQueryParameter("include_video", "false")
            .build()
            .toString()
    }
}