package com.luisherreralillo.filmica

import android.net.Uri

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

    private fun getUriBuilder() =
        Uri.Builder()
            .scheme("https")
            .authority("api.themoviedb.org")
            .appendPath("3")
            .appendQueryParameter("api_key", "f3d7b2c0ace82b34d96ee91121c22443")
}