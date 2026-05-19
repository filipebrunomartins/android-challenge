package com.challenge.movieflux

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.challenge.movieflux.core.domain.login.usecase.LogoutUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class AppE2ETest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var logoutUseCase: LogoutUseCase

    @Before
    fun setup() {
        hiltRule.inject()
        // Ensure we start from Login screen
        logoutUseCase()
    }

    @Test
    fun fullAppFlow_loginToFavorites() {
        // 1. Login Flow
        composeTestRule.onNodeWithText("Usuário").performTextInput("admin")
        composeTestRule.onNodeWithText("Senha").performTextInput("1234")
        composeTestRule.onNodeWithText("Entrar").performClick()

        // Handle Biometric Suggestion if it appears
        composeTestRule.mainClock.advanceTimeBy(1000)
        if (composeTestRule.onAllNodesWithText("Agora não").fetchSemanticsNodes().isNotEmpty()) {
            composeTestRule.onNodeWithText("Agora não").performClick()
        }

        // 2. Home Screen - Wait for movies to load
        composeTestRule.waitUntil(15000) {
            composeTestRule.onAllNodesWithContentDescription("Add to favorites").fetchSemanticsNodes().isNotEmpty()
        }

        // Click on the first movie card (the parent card of the favorite button)
        // Since we don't have tags, we can click the title if we find one, 
        // or just click the first item in the grid by its content description of the image.
        // The image has the title as content description.
        
        // Let's just find any node with click action that is not the favorite button.
        composeTestRule.onAllNodes(hasClickAction()).onFirst().performClick()

        // 3. Movie Detail Screen
        composeTestRule.onNodeWithContentDescription("Favorite").assertIsDisplayed()
        
        // Click favorite in details
        composeTestRule.onNodeWithContentDescription("Favorite").performClick()
        
        // Go back
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // 4. Navigate to Favorites using Bottom Bar
        composeTestRule.onNodeWithText("Favoritos").performClick()

        // 5. Favorites Screen
        composeTestRule.onNodeWithText("Meus Favoritos").assertIsDisplayed()
        
        // Verify that there is at least one movie card in favorites
        composeTestRule.onNodeWithContentDescription("Remove from favorites").assertIsDisplayed()
    }
}
