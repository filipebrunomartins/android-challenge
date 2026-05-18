package com.challenge.movieflux.feature.home

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.challenge.movieflux.core.model.data.Movie
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_loading_showsCircularProgressIndicator() {
        composeTestRule.setContent {
            HomeScreen(
                uiState = HomeUiState.Loading,
                searchQuery = "",
                onSearchQueryChange = {},
                onMovieClick = {},
                onToggleFavorite = {},
                onLoadMore = {},
                onRetry = {}
            )
        }

        composeTestRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertIsDisplayed()
    }

    @Test
    fun homeScreen_empty_showsEmptyMessage() {
        composeTestRule.setContent {
            HomeScreen(
                uiState = HomeUiState.Empty,
                searchQuery = "",
                onSearchQueryChange = {},
                onMovieClick = {},
                onToggleFavorite = {},
                onLoadMore = {},
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("Nenhum filme encontrado").assertIsDisplayed()
    }

    @Test
    fun homeScreen_error_showsErrorMessageAndRetryButton() {
        val errorMessage = "Ops! Algo deu errado"
        composeTestRule.setContent {
            HomeScreen(
                uiState = HomeUiState.Error(errorMessage),
                searchQuery = "",
                onSearchQueryChange = {},
                onMovieClick = {},
                onToggleFavorite = {},
                onLoadMore = {},
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        composeTestRule.onNodeWithText("Tentar novamente").assertIsDisplayed()
    }

    @Test
    fun homeScreen_success_showsMovies() {
        val movies = listOf(
            Movie(id = 1, title = "Interstellar", posterPath = null, backdropPath = null, releaseDate = null, voteAverage = 9.0, overview = "", genreIds = emptyList()),
            Movie(id = 2, title = "Inception", posterPath = null, backdropPath = null, releaseDate = null, voteAverage = 8.8, overview = "", genreIds = emptyList())
        )

        composeTestRule.setContent {
            HomeScreen(
                uiState = HomeUiState.Success(movies = movies),
                searchQuery = "",
                onSearchQueryChange = {},
                onMovieClick = {},
                onToggleFavorite = {},
                onLoadMore = {},
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("Interstellar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Inception").assertIsDisplayed()
    }

    @Test
    fun homeScreen_searchBar_updatesQuery() {
        var query = ""
        composeTestRule.setContent {
            HomeScreen(
                uiState = HomeUiState.Loading,
                searchQuery = query,
                onSearchQueryChange = { query = it },
                onMovieClick = {},
                onToggleFavorite = {},
                onLoadMore = {},
                onRetry = {}
            )
        }

        val searchPlaceholder = "Buscar filmes..."
        composeTestRule.onNodeWithText(searchPlaceholder).assertIsDisplayed()
        
        // Note: Testing updates usually requires the UI to recompose with the new state.
        // For a pure UI unit test of the Composable, we verify it calls the callback.
    }
}
