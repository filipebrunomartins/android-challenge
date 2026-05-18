package com.challenge.movieflux.core.domain.movie.usecase

import com.challenge.movieflux.core.domain.movie.repository.MovieRepository
import com.challenge.movieflux.core.model.data.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ToggleFavoriteUseCaseTest {

    private val repository: MovieRepository = mockk()
    private val useCase = ToggleFavoriteUseCase(repository)

    @Test
    fun `invoke should add favorite when movie is not favorite`() = runTest {
        // Given
        val movie = Movie(
            id = 1,
            title = "Test",
            posterPath = null,
            backdropPath = null,
            releaseDate = null,
            voteAverage = 0.0,
            overview = "",
            genreIds = emptyList()
        )
        every { repository.isMovieFavorite(movie.id) } returns flowOf(false)
        coEvery { repository.addFavoriteMovie(movie) } returns Unit

        // When
        useCase(movie)

        // Then
        coVerify { repository.addFavoriteMovie(movie) }
        coVerify(exactly = 0) { repository.removeFavoriteMovie(any()) }
    }

    @Test
    fun `invoke should remove favorite when movie is already favorite`() = runTest {
        // Given
        val movie = Movie(
            id = 1,
            title = "Test",
            posterPath = null,
            backdropPath = null,
            releaseDate = null,
            voteAverage = 0.0,
            overview = "",
            genreIds = emptyList()
        )
        every { repository.isMovieFavorite(movie.id) } returns flowOf(true)
        coEvery { repository.removeFavoriteMovie(movie) } returns Unit

        // When
        useCase(movie)

        // Then
        coVerify { repository.removeFavoriteMovie(movie) }
        coVerify(exactly = 0) { repository.addFavoriteMovie(any()) }
    }
}
