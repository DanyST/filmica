package com.luisherreralillo.filmica

import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

// Se mantendrá en memoria como unica instancia Singleton
object FilmsRepo {
    val films: MutableList<Film> = mutableListOf()
        /*get() {
            // WildCard field accede a la instancia de films y no al get
            if (field.isEmpty()) {
                field.addAll(dummyFilms())
            }

            return field
        }
*/
    fun findFilmById(id: String): Film? {
        return films.find { it.id == id }
    }

   /* private fun dummyFilms(): List<Film> {
        return (0..9).map {
           Film(
                title = "Film $it",
                genre = "Genre $it",
                release = "200$it-0$it-0$it",
                voteRating = it.toDouble(),
                overview = "Overview $it"
            )
        }
    }*/

    fun discoverFilms(context: Context,
                      callbackSuccess: (MutableList<Film>) -> Unit,
                      callbackError: (VolleyError) -> Unit) {

       if (this.films.isEmpty()) {
           requestDiscoverFilms(callbackSuccess, callbackError, context)
       } else {
           callbackSuccess.invoke(this.films)
       }
    }

    private fun requestDiscoverFilms(callbackSuccess: (MutableList<Film>) -> Unit,
                             callbackError: (VolleyError) -> Unit,
                             context: Context) {

        // JsonObjectRequest: instancia de una petición
        // obtiene toda la informacion de lo que va a realizar la peticion
        val url = ApiRoutes.discoverUrl()
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val newFilms = Film.parseFilms(response)
            this.films.addAll(newFilms)
            callbackSuccess.invoke(this.films)
        }, { error ->
            error.printStackTrace()
            callbackError.invoke(error)
        })

        Volley.newRequestQueue(context)
            .add(request)
    }
}