package com.challenge.movieflux.core.domain.movie.usecase

import com.challenge.movieflux.core.common.results.ApiUseCaseParams
import com.challenge.movieflux.core.common.results.BaseResult
import com.challenge.movieflux.core.domain.movie.repository.MovieRepository
import com.challenge.movieflux.core.model.data.MovieDetailResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class GetMovieDetailParams(
    val movieId: Int
)

class GetMovieDetailUseCase @Inject constructor(
    private val repository: MovieRepository
) : ApiUseCaseParams<GetMovieDetailParams, MovieDetailResponse> {

    override suspend fun execute(params: GetMovieDetailParams): Flow<BaseResult<MovieDetailResponse>> {
        return repository.getMovieDetails(params.movieId)
    }
}
