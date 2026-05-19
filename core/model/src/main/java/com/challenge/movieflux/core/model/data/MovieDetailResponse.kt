package com.challenge.movieflux.core.model.data

data class MovieDetailResponse(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: Double,
    val runtime: Int?,
    val genres: List<Genre>,
    val budget: Long?,
    val revenue: Long?,
    val status: String?
)

//Todo repensar
fun MovieDetailResponse.asMovie() = Movie(
    id = id,
    title = title,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    overview = overview,
    genreIds = genres.map { it.id }
)
