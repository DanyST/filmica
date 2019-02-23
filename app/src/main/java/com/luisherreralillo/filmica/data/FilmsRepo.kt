package com.luisherreralillo.filmica.data

import android.arch.persistence.room.Room
import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// Se mantendrá en memoria como unica instancia Singleton
object FilmsRepo {

    private var db: AppDatabase? = null

    private fun getDbInstance(context: Context): AppDatabase {
        if (db == null) {

            db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "filmica-db"
            ).build()
        }

        return db as AppDatabase
    }

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

       if (films.isEmpty()) {
           requestDiscoverFilms(callbackSuccess, callbackError, context)
       } else {
           callbackSuccess.invoke(films)
       }
    }

    fun saveFilm(
        context: Context,
        film: Film,
        callbackSuccess: (Film) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().insertFilm(film)
            }

            async.await()
            callbackSuccess.invoke(film)
        }

    }

    fun watchlist(
        context: Context
    ): List<Film> {

        val db = getDbInstance(context)
        db.filmDao().getFilms()
    }

    fun deleteFilm(
        context: Context,
        film: Film
    ): List<Film> {

        val db = getDbInstance(context)
        db.filmDao().deleteFilm(film)
    }

    private fun requestDiscoverFilms(callbackSuccess: (MutableList<Film>) -> Unit,
                                     callbackError: (VolleyError) -> Unit,
                                     context: Context) {

        // JsonObjectRequest: instancia de una petición
        // obtiene toda la informacion de lo que va a realizar la peticion
        val url = ApiRoutes.discoverUrl()
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val newFilms = Film.parseFilms(response)
            films.addAll(newFilms)
            callbackSuccess.invoke(films)
        }, { error ->
            error.printStackTrace()
            callbackError.invoke(error)
        })

        Volley.newRequestQueue(context)
            .add(request)
    }
}