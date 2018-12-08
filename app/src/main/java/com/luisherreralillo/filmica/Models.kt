package com.luisherreralillo.filmica

import java.util.*

data class Film(
    val id: String = UUID.randomUUID().toString(),
    var title: String,
    var genre: String,
    var release: String,
    var voteRating: Double,
    var overview: String
)