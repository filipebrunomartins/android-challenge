package com.challenge.movieflux.core.domain.movie.usecase

import com.challenge.movieflux.core.domain.movie.repository.MovieRepository
import com.challenge.movieflux.core.model.data.Movie
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) {
        val isFavorite = repository.isMovieFavorite(movie.id).first()
        if (isFavorite) {
            repository.removeFavoriteMovie(movie)
        } else {
            repository.addFavoriteMovie(movie)
        }
    }
}
