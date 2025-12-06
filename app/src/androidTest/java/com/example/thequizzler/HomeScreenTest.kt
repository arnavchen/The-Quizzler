package com.example.thequizzler

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.thequizzler.ui.screens.home.HomeScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented UI tests for the HomeScreen.
 */
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val playerName = "UITest!"

    @Test
    fun appName_isDisplayed_onHomeScreen() {

        composeTestRule.setContent {
            val navController = rememberNavController()
            HomeScreen(navController = navController)
        }

        // Get title
        val titleNode = composeTestRule.onNodeWithText("The Quizzler")

        // Ensure it is being displayed
        titleNode.assertIsDisplayed()
    }

    @Test
    fun nameTextField_acceptsInput() {

        composeTestRule.setContent {
            val navController = rememberNavController()
            HomeScreen(navController = navController)
        }

        // Get name field and then input a name
        val nameTextField = composeTestRule.onNodeWithText("Enter your name")
        nameTextField.performTextClearance()
        nameTextField.performTextInput(playerName)

        // See if there is the entered name is on the screen
        composeTestRule.onNodeWithText(playerName).assertIsDisplayed()
    }
}
