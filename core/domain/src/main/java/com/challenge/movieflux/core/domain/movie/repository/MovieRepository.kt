package com.challenge.movieflux.core.domain.movie.repository

import com.challenge.movieflux.core.common.results.BaseResult
import com.challenge.movieflux.core.model.data.Movie
import com.challenge.movieflux.core.model.data.PopularMoviesResponse
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): Flow<BaseResult<PopularMoviesResponse>>
    suspend fun searchMovies(query: String, page: Int): Flow<BaseResult<PopularMoviesResponse>>
}
