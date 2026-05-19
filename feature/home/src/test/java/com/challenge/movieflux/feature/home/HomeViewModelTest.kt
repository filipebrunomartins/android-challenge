package com.challenge.movieflux.feature.home

import app.cash.turbine.test
import com.challenge.movieflux.core.common.results.BaseResult
import com.challenge.movieflux.core.domain.movie.usecase.GetFavoriteMovieIdsUseCase
import com.challenge.movieflux.core.domain.movie.usecase.GetPopularMoviesUseCase
import com.challenge.movieflux.core.domain.movie.usecase.SearchMoviesUseCase
import com.challenge.movieflux.core.domain.movie.usecase.ToggleFavoriteUseCase
import com.challenge.movieflux.core.model.data.Movie
import com.challenge.movieflux.core.model.data.PopularMoviesResponse
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
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
class HomeViewModelTest {

    private val getPopularMoviesUseCase: GetPopularMoviesUseCase = mockk()
    private val searchMoviesUseCase: SearchMoviesUseCase = mockk()
    private val getFavoriteMovieIdsUseCase: GetFavoriteMovieIdsUseCase = mockk()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getFavoriteMovieIdsUseCase() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = HomeViewModel(
        getPopularMoviesUseCase,
        searchMoviesUseCase,
        getFavoriteMovieIdsUseCase,
        toggleFavoriteUseCase
    )

    @Test
    fun `initial state should be Loading`() = runTest {
        coEvery { getPopularMoviesUseCase.execute(any()) } returns flowOf(BaseResult.Loading)
        
        val viewModel = createViewModel()
        assertEquals(HomeUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `load movies success should update state to Success`() = runTest {
        val movies = listOf(
            Movie(1, "Movie 1", null, null, null, 8.0, "Overview", emptyList())
        )
        val response = PopularMoviesResponse(1, movies, 10, 100)
        
        val flow = MutableSharedFlow<BaseResult<PopularMoviesResponse>>(replay = 1)
        coEvery { getPopularMoviesUseCase.execute(any()) } returns flow

        val viewModel = createViewModel()
        
        viewModel.uiState.test {
            assertEquals(HomeUiState.Loading, awaitItem())
            
            flow.emit(BaseResult.Success(response))
            testDispatcher.scheduler.advanceUntilIdle()
            
            val items = cancelAndConsumeRemainingEvents()
            // Using a simple filter and checking type to avoid IDE issues with filterIsInstance in some environments
            val successState = items.mapNotNull { it.getEvent() }.filterIsInstance<HomeUiState.Success>().lastOrNull()
            
            assertTrue("Expected Success state in events", successState != null)
            assertEquals(movies, successState?.movies)
        }
    }

    @Test
    fun `load movies failure should update state to Error`() = runTest {
        val errorMessage = "Error fetching movies"
        val flow = MutableSharedFlow<BaseResult<PopularMoviesResponse>>(replay = 1)
        coEvery { getPopularMoviesUseCase.execute(any()) } returns flow

        val viewModel = createViewModel()
        
        viewModel.uiState.test {
            assertEquals(HomeUiState.Loading, awaitItem())
            
            flow.emit(BaseResult.Error(errorMessage, 500))
            testDispatcher.scheduler.advanceUntilIdle()
            
            val items = cancelAndConsumeRemainingEvents()
            val errorState = items.mapNotNull { it.getEvent() }.filterIsInstance<HomeUiState.Error>().lastOrNull()
            
            assertTrue("Expected Error state in events", errorState != null)
            assertEquals(errorMessage, errorState?.message)
        }
    }

    @Test
    fun `search query change should trigger search after debounce`() = runTest {
        val query = "Matrix"
        val movies = listOf(Movie(1, "Matrix", null, null, null, 9.0, "Overview", emptyList()))
        val response = PopularMoviesResponse(1, movies, 1, 1)

        coEvery { getPopularMoviesUseCase.execute(any()) } returns flowOf(BaseResult.Success(PopularMoviesResponse(1, emptyList(), 1, 0)))
        
        val searchFlow = MutableSharedFlow<BaseResult<PopularMoviesResponse>>(replay = 1)
        coEvery { searchMoviesUseCase.execute(any()) } returns searchFlow

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle() 

        viewModel.onSearchQueryChanged(query)
        
        viewModel.uiState.test {
            // Advance time for debounce (500ms)
            testDispatcher.scheduler.advanceTimeBy(600)
            
            searchFlow.emit(BaseResult.Success(response))
            testDispatcher.scheduler.advanceUntilIdle()
            
            val items = cancelAndConsumeRemainingEvents()
            val successState = items.mapNotNull { it.getEvent() }.filterIsInstance<HomeUiState.Success>().lastOrNull { it.isSearching }
            
            assertTrue("Expected Success state with searching=true in events", successState != null)
            assertEquals(movies, successState?.movies)
        }
    }
    
    // Helper extension to extract event from Turbine Event enum
    private fun <T> app.cash.turbine.Event<T>.getEvent(): T? {
        return when (this) {
            is app.cash.turbine.Event.Item -> value
            else -> null
        }
    }
}
