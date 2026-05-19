package com.challenge.movieflux.core.domain.movie.usecase

import com.challenge.movieflux.core.common.results.ApiUseCaseParams
import com.challenge.movieflux.core.common.results.BaseResult
import com.challenge.movieflux.core.domain.movie.repository.MovieRepository
import com.challenge.movieflux.core.model.data.Movie
import com.challenge.movieflux.core.model.data.PopularMoviesResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class SearchMoviesParams(
    val query: String,
    val page: Int
)

class SearchMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
): ApiUseCaseParams<SearchMoviesParams, PopularMoviesResponse> {

    override suspend fun execute(params: SearchMoviesParams): Flow<BaseResult<PopularMoviesResponse>> {
        return repository.searchMovies(params.query, params.page)
    }
}
