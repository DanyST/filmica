package com.luisherreralillo.filmica.data

import android.net.Uri
import com.luisherreralillo.filmica.BuildConfig

object ApiRoutes {
    fun discoverUrl(language: String = "en-US", sortBy: String = "popularity.desc", page: Int = 1): String {
        return getUriBuilder()
            .appendPath("discover")
            .appendPath("movie")
            .appendQueryParameter("language", language)
            .appendQueryParameter("sort_by", sortBy)
            .appendQueryParameter("page", page.toString())
            .appendQueryParameter("include_adult", "false")
            .appendQueryParameter("include_video", "false")
            .build()
            .toString()
    }

    fun trendingUrl(language: String = "en-US", page: Int = 1): String {
        return getUriBuilder()
            .appendPath("trending")
            .appendPath("movie")
            .appendPath("week")
            .appendQueryParameter("language", language)
            .appendQueryParameter("page", page.toString())
            .appendQueryParameter("include_adult", "false")
            .appendQueryParameter("include_video", "false")
            .build()
            .toString()
    }

    fun searchUrl(query: String = "", language: String = "en-US", page: Int = 1): String {
        return getUriBuilder()
            .appendPath("search")
            .appendPath("movie")
            .appendQueryParameter("language", language)
            .appendQueryParameter("page", page.toString())
            .appendQueryParameter("include_adult", "false")
            .appendQueryParameter("query", query)
            .build()
            .toString()
    }

    private fun getUriBuilder() =
        Uri.Builder()
            .scheme("https")
            .authority("api.themoviedb.org")
            .appendPath("3")
            .appendQueryParameter("api_key", BuildConfig.MovieDBApiKey)
}