package com.challenge.movieflux.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.challenge.movieflux.core.model.data.Movie

@Entity(tableName = "favorite_movies")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: Double,
    val overview: String,
)

fun MovieEntity.asExternalModel() = Movie(
    id = id,
    title = title,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    overview = overview,
    genreIds = emptyList(),
    isFavorite = true
)

fun Movie.asEntity() = MovieEntity(
    id = id,
    title = title,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    overview = overview,
)
