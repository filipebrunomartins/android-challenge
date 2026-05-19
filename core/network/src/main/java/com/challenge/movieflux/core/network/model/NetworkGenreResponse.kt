package com.challenge.movieflux.core.network.model

import com.challenge.movieflux.core.model.data.Genre
import com.challenge.movieflux.core.model.data.GenreResponse

data class NetworkGenreResponse(
    val genres: List<NetworkGenre>
)

data class NetworkGenre(
    val id: Int,
    val name: String
)
