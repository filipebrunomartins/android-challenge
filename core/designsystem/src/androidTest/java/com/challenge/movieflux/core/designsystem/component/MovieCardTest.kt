package com.challenge.movieflux.core.designsystem.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

class MovieCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun movieCard_displaysTitle() {
        val title = "The Matrix"
        composeTestRule.setContent {
            MovieCard(
                title = title,
                posterPath = null,
                isFavorite = false,
                onMovieClick = {},
                onToggleFavorite = {}
            )
        }

        composeTestRule.onNodeWithText(title).assertIsDisplayed()
    }

    @Test
    fun movieCard_onMovieClick_isCalled() {
        val movieClicked = AtomicBoolean(false)
        composeTestRule.setContent {
            MovieCard(
                title = "Test",
                posterPath = null,
                isFavorite = false,
                onMovieClick = { movieClicked.set(true) },
                onToggleFavorite = {}
            )
        }

        composeTestRule.onNodeWithText("Test").performClick()
        assert(movieClicked.get())
    }

    @Test
    fun movieCard_onFavoriteClick_isCalled() {
        val favoriteClicked = AtomicBoolean(false)
        composeTestRule.setContent {
            MovieCard(
                title = "Test",
                posterPath = null,
                isFavorite = false,
                onMovieClick = {},
                onToggleFavorite = { favoriteClicked.set(true) }
            )
        }

        composeTestRule.onNodeWithContentDescription("Add to favorites").performClick()
        assert(favoriteClicked.get())
    }

    @Test
    fun movieCard_showsRemoveIcon_whenFavorite() {
        composeTestRule.setContent {
            MovieCard(
                title = "Test",
                posterPath = null,
                isFavorite = true,
                onMovieClick = {},
                onToggleFavorite = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Remove from favorites").assertIsDisplayed()
    }
}
