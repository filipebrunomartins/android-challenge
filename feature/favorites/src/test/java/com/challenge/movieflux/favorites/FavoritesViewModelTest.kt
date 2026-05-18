package com.challenge.movieflux.favorites

import app.cash.turbine.test
import com.challenge.movieflux.core.domain.movie.usecase.GetFavoriteMoviesUseCase
import com.challenge.movieflux.core.domain.movie.usecase.ToggleFavoriteUseCase
import com.challenge.movieflux.core.model.data.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase = mockk()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = FavoritesViewModel(
        getFavoriteMoviesUseCase,
        toggleFavoriteUseCase
    )

    @Test
    fun `initial state should be Loading`() = runTest {
        every { getFavoriteMoviesUseCase() } returns flowOf(emptyList())
        val viewModel = createViewModel()
        assertEquals(FavoritesUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `when favorite movies is empty should emit Empty state`() = runTest {
        every { getFavoriteMoviesUseCase() } returns flowOf(emptyList())
        
        val viewModel = createViewModel()
        
        viewModel.uiState.test {
            assertEquals(FavoritesUiState.Loading, awaitItem())
            
            advanceUntilIdle()
            
            assertEquals(FavoritesUiState.Empty, awaitItem())
        }
    }

    @Test
    fun `when favorite movies is not empty should emit Success state`() = runTest {
        val movies = listOf(
            Movie(1, "Movie 1", null, null, null, 8.0, "Overview", emptyList())
        )
        every { getFavoriteMoviesUseCase() } returns flowOf(movies)
        
        val viewModel = createViewModel()
        
        viewModel.uiState.test {
            assertEquals(FavoritesUiState.Loading, awaitItem())
            
            advanceUntilIdle()
            
            val state = awaitItem()
            assertTrue(state is FavoritesUiState.Success)
            assertEquals(movies, (state as FavoritesUiState.Success).movies)
        }
    }

    @Test
    fun `toggleFavorite should call toggleFavoriteUseCase`() = runTest {
        val movie = Movie(1, "Movie 1", null, null, null, 8.0, "Overview", emptyList())
        every { getFavoriteMoviesUseCase() } returns flowOf(emptyList())
        coEvery { toggleFavoriteUseCase(movie) } returns Unit
        
        val viewModel = createViewModel()
        viewModel.toggleFavorite(movie)
        
        advanceUntilIdle()
        
        coVerify { toggleFavoriteUseCase(movie) }
    }
}
