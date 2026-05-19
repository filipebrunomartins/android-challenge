package com.challenge.movieflux.core.data.repository

import app.cash.turbine.test
import com.challenge.movieflux.core.common.results.BaseResult
import com.challenge.movieflux.core.database.dao.FavoriteDao
import com.challenge.movieflux.core.data.utils.NetworkBoundResource
import com.challenge.movieflux.core.network.mapper.GenreResponseMapper
import com.challenge.movieflux.core.network.mapper.MovieDetailResponseMapper
import com.challenge.movieflux.core.network.mapper.PopularMoviesResponseMapper
import com.challenge.movieflux.core.network.model.NetworkPopularMoviesResponse
import com.challenge.movieflux.core.network.retrofit.RetrofitMovieFluxNetworkApi
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class MovieRepositoryImplTest {

    private lateinit var networkApi: RetrofitMovieFluxNetworkApi
    private lateinit var favoriteDao: FavoriteDao
    private lateinit var networkBoundResources: NetworkBoundResource
    private lateinit var popularMoviesMapper: PopularMoviesResponseMapper
    private lateinit var movieDetailMapper: MovieDetailResponseMapper
    private lateinit var genreResponseMapper: GenreResponseMapper
    private lateinit var repository: MovieRepositoryImpl

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        networkApi = mockk()
        favoriteDao = mockk()
        networkBoundResources = NetworkBoundResource(testDispatcher)
        
        // Mappers are usually pure logic, we can use real ones or mock them.
        // Using real ones for more "integrated" unit test, or mocks for pure unit.
        // Let's mock them to follow strict unit test pattern.
        popularMoviesMapper = mockk()
        movieDetailMapper = mockk()
        genreResponseMapper = mockk()

        repository = MovieRepositoryImpl(
            networkApi,
            favoriteDao,
            networkBoundResources,
            popularMoviesMapper,
            movieDetailMapper,
            genreResponseMapper
        )
    }

    @Test
    fun `getPopularMovies should emit Loading then Success when API call is successful`() = runTest {
        // Given
        val page = 1
        val networkResponse = NetworkPopularMoviesResponse(
            page = 1,
            results = emptyList(),
            totalPages = 1,
            totalResults = 0
        )
        val domainResponse = mockk<com.challenge.movieflux.core.model.data.PopularMoviesResponse>()

        coEvery { networkApi.getPopularMovies(page) } returns Response.success(networkResponse)
        every { popularMoviesMapper.mapFromApiResponse(networkResponse) } returns domainResponse

        // When & Then
        repository.getPopularMovies(page).test {
            assertEquals(BaseResult.Loading, awaitItem())
            val success = awaitItem()
            assertTrue(success is BaseResult.Success)
            assertEquals(domainResponse, (success as BaseResult.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `getPopularMovies should emit Loading then Error when API call fails`() = runTest {
        // Given
        val page = 1
        coEvery { networkApi.getPopularMovies(page) } throws java.io.IOException("No internet")

        // When & Then
        repository.getPopularMovies(page).test {
            assertEquals(BaseResult.Loading, awaitItem())
            val error = awaitItem()
            assertTrue(error is BaseResult.Error)
            awaitComplete()
        }
    }

    @Test
    fun `getFavoriteMovieIds should return flow from DAO`() = runTest {
        // Given
        val expectedIds = listOf(1, 2, 3)
        every { favoriteDao.getFavoriteMovieIds() } returns flowOf(expectedIds)

        // When & Then
        repository.getFavoriteMovieIds().test {
            assertEquals(expectedIds, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `isMovieFavorite should return flow from DAO`() = runTest {
        // Given
        val movieId = 123
        every { favoriteDao.isMovieFavorite(movieId) } returns flowOf(true)

        // When & Then
        repository.isMovieFavorite(movieId).test {
            assertTrue(awaitItem())
            awaitComplete()
        }
    }
}
