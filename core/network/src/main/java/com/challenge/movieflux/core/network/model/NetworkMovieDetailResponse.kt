package com.challenge.movieflux.core.network.model

import com.challenge.movieflux.core.model.data.MovieDetailResponse
import kotlinx.serialization.SerialName

data class NetworkMovieDetailResponse(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("release_date")
    val releaseDate: String?,
    @SerialName("vote_average")
    val voteAverage: Double,
    val runtime: Int?,
    val genres: List<NetworkGenre>,
    val budget: Long?,
    val revenue: Long?,
    val status: String?
)
