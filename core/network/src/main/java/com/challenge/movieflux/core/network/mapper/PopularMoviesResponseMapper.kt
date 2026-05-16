package com.challenge.movieflux.core.network.mapper

import android.util.Log
import com.challenge.movieflux.core.model.data.Movie
import com.challenge.movieflux.core.model.data.PopularMoviesResponse
import com.challenge.movieflux.core.network.model.NetworkMovie
import com.challenge.movieflux.core.network.model.NetworkPopularMoviesResponse
import com.challenge.movieflux.core.network.utils.Mapper
import javax.inject.Inject

class PopularMoviesResponseMapper @Inject constructor(
    private val mapper: MovieMapper
) : Mapper<NetworkPopularMoviesResponse, PopularMoviesResponse> {

    override fun mapFromApiResponse(type: NetworkPopularMoviesResponse): PopularMoviesResponse {
        return PopularMoviesResponse(
            page = type.page,
            results = type.results.map { mapper.mapFromApiResponse(it) },
            totalPages = type.totalPages,
            totalResults = type.totalResults
        )
    }
}

class MovieMapper @Inject constructor() : Mapper<NetworkMovie, Movie> {

    override fun mapFromApiResponse(type: NetworkMovie): Movie {
        return Movie(
            id = type.id,
            title = type.title,
            posterPath = type.posterPath,
            backdropPath = type.backdropPath,
            releaseDate = type.releaseDate,
            voteAverage = type.voteAverage,
            overview = type.overview,
            genreIds = type.genreIds ?: emptyList()
        )
    }
}