package com.challenge.movieflux.core.network.mapper

import com.challenge.movieflux.core.model.data.MovieDetailResponse
import com.challenge.movieflux.core.network.model.NetworkMovieDetailResponse
import com.challenge.movieflux.core.network.utils.Mapper
import javax.inject.Inject

class MovieDetailResponseMapper @Inject constructor(
    private val mapper: GenreMapper
) : Mapper<NetworkMovieDetailResponse, MovieDetailResponse> {

    override fun mapFromApiResponse(type: NetworkMovieDetailResponse): MovieDetailResponse {
        return  MovieDetailResponse(
            id = type.id,
            title = type.title,
            overview = type.overview,
            posterPath = type.posterPath,
            backdropPath = type.backdropPath,
            releaseDate = type.releaseDate,
            voteAverage = type.voteAverage,
            runtime = type.runtime,
            genres =  type.genres.map { mapper.mapFromApiResponse(it) },
            budget = type.budget,
            revenue = type.revenue,
            status = type.status
        )
    }
}