package com.challenge.movieflux.core.domain.movie.usecase

import com.challenge.movieflux.core.domain.movie.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsMovieFavoriteUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int): Flow<Boolean> = repository.isMovieFavorite(movieId)
}
