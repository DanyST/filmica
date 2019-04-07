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

const val TRENDING_FILM_TYPE = "trends"
const val DISCOVER_FILM_TYPE = "discover"
const val SEARCH_FILM_TYPE = "search"

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

    // Lista generica de peliculas que va cambiando dependiendo del fragmento
    // y busquedas en la funcion findFilmById
    //val films: MutableList<Film> = mutableListOf()
    /*get() {
        // WildCard field accede a la instancia de films y no al get
        if (field.isEmpty()) {
            field.addAll(dummyFilms())
        }

        return field
    }
    */

    private val discoverFilmsList: MutableList<Film> = mutableListOf()
    private val trendingFilmList: MutableList<Film> = mutableListOf()
    private val searchFilmList: MutableList<Film> = mutableListOf()

    fun findFilmById(id: String): Film? {
        return discoverFilmsList.find { it.id == id } ?: trendingFilmList.find { it.id == id }
        ?: searchFilmList.find { it.id == id }
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

    fun discoverFilms(
        context: Context,
        page: Int,
        callbackSuccess: (MutableList<Film>) -> Unit,
        callbackError: (VolleyError) -> Unit
    ) {

        if (discoverFilmsList.isEmpty() || page > 1) {
            val url = ApiRoutes.discoverUrl(page = page)
            requestFilms(callbackSuccess, callbackError, context, url, DISCOVER_FILM_TYPE)
        } else if(discoverFilmsList.isNotEmpty() && page == 1) {
            callbackSuccess.invoke(discoverFilmsList)
        }

    }

    fun trendingFilms(
        context: Context,
        page: Int,
        callbackSuccess: (MutableList<Film>) -> Unit,
        callbackError: (VolleyError) -> Unit
    ) {

        if (trendingFilmList.isEmpty() || page > 1) {
            val url = ApiRoutes.trendingUrl(page = page)
            requestFilms(callbackSuccess, callbackError, context, url, TRENDING_FILM_TYPE)
        } else if(trendingFilmList.isNotEmpty() && page == 1) {
            callbackSuccess.invoke(trendingFilmList)
        }
    }

    fun searchFilms(
        context: Context,
        query: String,
        callbackSuccess: (MutableList<Film>) -> Unit,
        callbackError: (VolleyError) -> Unit
    ) {

        searchFilmList.clear()

        val url = ApiRoutes.searchUrl(query = query)
        requestFilms(callbackSuccess, callbackError, context, url, SEARCH_FILM_TYPE)
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
        context: Context,
        callbackSuccess: (List<Film>) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().getFilms()
            }

            val films: List<Film> = async.await()
            callbackSuccess.invoke(films)
        }


    }

    fun deleteFilm(
        context: Context,
        film: Film,
        callbackSuccess: (Film) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().deleteFilm(film)
            }

            async.await()
            callbackSuccess.invoke(film)
        }

    }

    private fun requestFilms(
        callbackSuccess: (MutableList<Film>) -> Unit,
        callbackError: (VolleyError) -> Unit,
        context: Context,
        url: String,
        typeFilm: String
    ) {

        // JsonObjectRequest: instancia de una petición
        // obtiene toda la informacion de lo que va a realizar la peticion
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val newFilms = Film.parseFilms(response)

            when (typeFilm) {
                TRENDING_FILM_TYPE -> {
                    trendingFilmList.clear()
                    trendingFilmList.addAll(newFilms)
                    callbackSuccess.invoke(trendingFilmList)
                }
                DISCOVER_FILM_TYPE -> {
                    discoverFilmsList.clear()
                    discoverFilmsList.addAll(newFilms)
                    callbackSuccess.invoke(discoverFilmsList)
                }
                SEARCH_FILM_TYPE -> {
                    searchFilmList.addAll(newFilms)
                    callbackSuccess.invoke(searchFilmList)
                }
            }

        }, { error ->
            error.printStackTrace()
            callbackError.invoke(error)
        })

        Volley.newRequestQueue(context)
            .add(request)
    }

}