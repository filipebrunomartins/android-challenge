package com.challenge.movieflux.core.network.model

import com.challenge.movieflux.core.model.data.Movie
import com.challenge.movieflux.core.model.data.PopularMoviesResponse
import kotlinx.serialization.SerialName

data class NetworkPopularMoviesResponse(
    val page: Int,
    val results: List<NetworkMovie>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)

data class NetworkMovie(
    val id: Int,
    val title: String,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double,
    val overview: String,
    @SerialName("genre_ids")
    val genreIds: List<Int>
)

