package com.challenge.movieflux.favorites

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.challenge.movieflux.core.model.data.Movie
import org.junit.Rule
import org.junit.Test

class FavoritesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun favoritesScreen_loading_showsCircularProgressIndicator() {
        composeTestRule.setContent {
            FavoritesScreen(
                uiState = FavoritesUiState.Loading,
                onMovieClick = {},
                onToggleFavorite = {}
            )
        }

        composeTestRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertIsDisplayed()
    }

    @Test
    fun favoritesScreen_empty_showsEmptyMessage() {
        composeTestRule.setContent {
            FavoritesScreen(
                uiState = FavoritesUiState.Empty,
                onMovieClick = {},
                onToggleFavorite = {}
            )
        }

        composeTestRule.onNodeWithText("Você ainda não favoritou nenhum filme.").assertIsDisplayed()
    }

    @Test
    fun favoritesScreen_success_showsFavoriteMovies() {
        val favoriteMovies = listOf(
            Movie(
                id = 1,
                title = "Interstellar",
                posterPath = null,
                backdropPath = null,
                releaseDate = null,
                voteAverage = 9.0,
                overview = "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
                genreIds = emptyList(),
                isFavorite = true
            ),
            Movie(
                id = 2,
                title = "Inception",
                posterPath = null,
                backdropPath = null,
                releaseDate = null,
                voteAverage = 8.8,
                overview = "A thief who steals corporate secrets through the use of dream-sharing technology.",
                genreIds = emptyList(),
                isFavorite = true
            )
        )

        composeTestRule.setContent {
            FavoritesScreen(
                uiState = FavoritesUiState.Success(movies = favoriteMovies),
                onMovieClick = {},
                onToggleFavorite = {}
            )
        }

        composeTestRule.onNodeWithText("Interstellar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Inception").assertIsDisplayed()
    }

    @Test
    fun favoritesScreen_movieClick_callsCallback() {
        var clickedMovieId = -1
        val movie = Movie(
            id = 42,
            title = "The Hitchhiker's Guide to the Galaxy",
            posterPath = null,
            backdropPath = null,
            releaseDate = null,
            voteAverage = 6.8,
            overview = "Don't Panic.",
            genreIds = emptyList(),
            isFavorite = true
        )

        composeTestRule.setContent {
            FavoritesScreen(
                uiState = FavoritesUiState.Success(movies = listOf(movie)),
                onMovieClick = { clickedMovieId = it },
                onToggleFavorite = {}
            )
        }

        composeTestRule.onNodeWithText("The Hitchhiker's Guide to the Galaxy").performClick()
        assert(clickedMovieId == 42)
    }
}
