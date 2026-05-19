package com.challenge.movieflux.core.domain.movie.usecase

import com.challenge.movieflux.core.common.results.ApiUseCaseNonParams
import com.challenge.movieflux.core.common.results.BaseResult
import com.challenge.movieflux.core.domain.movie.repository.MovieRepository
import com.challenge.movieflux.core.model.data.GenreResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGenresUseCase @Inject constructor(
    private val repository: MovieRepository
) : ApiUseCaseNonParams<GenreResponse> {

    override suspend fun execute(): Flow<BaseResult<GenreResponse>> {
        return repository.getGenres()
    }
}
