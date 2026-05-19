package com.challenge.movieflux.core.domain.movie.usecase

import com.challenge.movieflux.core.common.results.ApiUseCaseParams
import com.challenge.movieflux.core.common.results.BaseResult
import com.challenge.movieflux.core.domain.movie.repository.MovieRepository
import com.challenge.movieflux.core.model.data.PopularMoviesResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


data class GetPopularMoviesParams(
    val page: Int
)

class GetPopularMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
): ApiUseCaseParams<GetPopularMoviesParams, PopularMoviesResponse> {

    override suspend fun execute(params: GetPopularMoviesParams): Flow<BaseResult<PopularMoviesResponse>> {
        return repository.getPopularMovies(params.page)
    }
}
