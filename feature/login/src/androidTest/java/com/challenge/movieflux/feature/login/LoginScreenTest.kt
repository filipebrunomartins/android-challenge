package com.challenge.movieflux.feature.login

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.challenge.movieflux.core.designsystem.theme.MovieFluxTheme
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_initialState_showsAllFields() {
        composeTestRule.setContent {
            MovieFluxTheme {
                // Testing a "stateless" version or simply the UI structure
                // Since LoginScreen needs a ViewModel, we verify the components it renders
                LoginScreen(
                    onLoginSuccess = {}
                )
            }
        }

        // Check if "Login" title exists
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()

        // Check if User field exists
        composeTestRule.onNodeWithText("Usuário").assertIsDisplayed()

        // Check if Password field exists
        composeTestRule.onNodeWithText("Senha").assertIsDisplayed()

        // Check if Login button exists
        composeTestRule.onNodeWithText("Entrar").assertIsDisplayed()
    }

    @Test
    fun loginScreen_typingCredentials_updatesFields() {
        composeTestRule.setContent {
            MovieFluxTheme {
                LoginScreen(onLoginSuccess = {})
            }
        }

        val userText = "admin"
        val passText = "1234"

        // Type username
        composeTestRule.onNodeWithText("Usuário").performTextInput(userText)
        composeTestRule.onNodeWithText(userText).assertIsDisplayed()

        // Type password
        composeTestRule.onNodeWithText("Senha").performTextInput(passText)
        // Note: Password field might have transformations, so we search by label or tag if it had one
        // But since we just typed, the node with that text should exist (or be hidden by bullets)
    }

    @Test
    fun loginScreen_button_isClickable() {
        composeTestRule.setContent {
            MovieFluxTheme {
                LoginScreen(onLoginSuccess = {})
            }
        }

        composeTestRule.onNodeWithText("Entrar").assertIsEnabled()
        composeTestRule.onNodeWithText("Entrar").performClick()
    }
}
