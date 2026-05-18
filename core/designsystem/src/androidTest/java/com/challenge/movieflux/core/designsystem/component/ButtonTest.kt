package com.challenge.movieflux.core.designsystem.component

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

class ButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun movieFluxButton_displaysText() {
        composeTestRule.setContent {
            MovieFluxButton(onClick = {}, text = { Text("Click me") })
        }

        composeTestRule.onNodeWithText("Click me").assertIsDisplayed()
    }

    @Test
    fun movieFluxButton_onButtonClick_isCalled() {
        val buttonClicked = AtomicBoolean(false)
        composeTestRule.setContent {
            MovieFluxButton(onClick = { buttonClicked.set(true) }, text = { Text("Click me") })
        }

        composeTestRule.onNodeWithText("Click me").performClick()
        assert(buttonClicked.get())
    }

    @Test
    fun movieFluxButton_whenDisabled_isNotClickable() {
        val buttonClicked = AtomicBoolean(false)
        composeTestRule.setContent {
            MovieFluxButton(
                onClick = { buttonClicked.set(true) },
                enabled = false,
                text = { Text("Click me") }
            )
        }

        composeTestRule.onNodeWithText("Click me").assertIsNotEnabled()
        composeTestRule.onNodeWithText("Click me").performClick()
        assert(!buttonClicked.get())
    }
}
