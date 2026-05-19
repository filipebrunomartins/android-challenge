package com.challenge.movieflux.core.domain.movie.usecase

import com.challenge.movieflux.core.domain.movie.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteMovieIdsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<List<Int>> = repository.getFavoriteMovieIds()
}
