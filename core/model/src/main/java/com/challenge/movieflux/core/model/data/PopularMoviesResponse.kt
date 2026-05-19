package com.challenge.movieflux.core.model.data

data class PopularMoviesResponse(
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int,
    val totalResults: Int
)

data class Movie(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String? = null,
    val voteAverage: Double,
    val overview: String,
    val genreIds: List<Int>,
    val isFavorite: Boolean = false
)
