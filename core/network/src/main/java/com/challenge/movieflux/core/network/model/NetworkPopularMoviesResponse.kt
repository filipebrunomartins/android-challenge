package com.challenge.movieflux.core.network.model

import com.google.gson.annotations.SerializedName

data class NetworkPopularMoviesResponse(
    val page: Int,
    val results: List<NetworkMovie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

data class NetworkMovie(
    val id: Int,
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("release_date")
    val releaseDate: String? = null,
    @SerializedName("vote_average")
    val voteAverage: Double,
    val overview: String,
    @SerializedName("genre_ids")
    val genreIds: List<Int>?
)

