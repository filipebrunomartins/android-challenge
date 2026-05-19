package com.challenge.movieflux.core.domain.movie.usecase

import app.cash.turbine.test
import com.challenge.movieflux.core.common.results.BaseResult
import com.challenge.movieflux.core.domain.movie.repository.MovieRepository
import com.challenge.movieflux.core.model.data.PopularMoviesResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetPopularMoviesUseCaseTest {

    private val repository: MovieRepository = mockk()
    private val useCase = GetPopularMoviesUseCase(repository)

    @Test
    fun `execute should return flow of BaseResult from repository`() = runTest {
        // Given
        val params = GetPopularMoviesParams(page = 1)
        val expectedResult = BaseResult.Success(
            mockk<PopularMoviesResponse>()
        )
        coEvery { repository.getPopularMovies(params.page) } returns flowOf(expectedResult)

        // When
        val result = useCase.execute(params)

        // Then
        result.test {
            assertEquals(expectedResult, awaitItem())
            awaitComplete()
        }
    }
}
