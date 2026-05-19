package com.challenge.movieflux.core.network.model

import com.google.gson.annotations.SerializedName

data class NetworkMovieDetailResponse(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("vote_average")
    val voteAverage: Double,
    val runtime: Int?,
    val genres: List<NetworkGenre>,
    val budget: Long?,
    val revenue: Long?,
    val status: String?
)
