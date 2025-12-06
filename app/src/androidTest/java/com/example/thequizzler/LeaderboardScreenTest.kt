package com.example.thequizzler

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.thequizzler.ui.screens.leaderboard.PlayerScore
import com.example.thequizzler.ui.screens.leaderboard.VerticalLeaderboardScreen // We directly test this
import com.example.thequizzler.ui.theme.TheQuizzlerTheme
import org.junit.Rule
import org.junit.Test

/**
 * Simple, isolated UI tests for the LeaderboardScreen's layout composables.
 * This test does NOT use a real ViewModel. Instead, it passes fake data
 * directly to the UI to verify that it displays correctly.
 */
class LeaderboardScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // A fake list of scores to use in our tests.
    private val fakeLeaderboardData = listOf(
        PlayerScore("Alice", 980, 1),
        PlayerScore("Bob", 850, 2),
        PlayerScore("Charlie", 720, 3)
    )

    @Test
    fun leaderboardTitle_isDisplayed() {
        // Set the content to be the VerticalLeaderboardScreen with an empty list.
        // This completely bypasses the main LeaderboardScreen and its broken ViewModel factory.
        composeTestRule.setContent {
            TheQuizzlerTheme {
                VerticalLeaderboardScreen(leaderboard = emptyList())
            }
        }

        // Verify the title "Leaderboard" is on the screen
        composeTestRule.onNodeWithText("Leaderboard").assertIsDisplayed()
    }

    @Test
    fun playerScores_areDisplayedCorrectly_inList() {
        // Set the content with our fake data
        composeTestRule.setContent {
            TheQuizzlerTheme {
                VerticalLeaderboardScreen(leaderboard = fakeLeaderboardData)
            }
        }

        // Verify that the data for the first player is displayed
        composeTestRule.onNodeWithText("Alice").assertIsDisplayed()
        composeTestRule.onNodeWithText("980 pts").assertIsDisplayed()

        // Verify that the data for the second player is also displayed
        composeTestRule.onNodeWithText("Bob").assertIsDisplayed()
        composeTestRule.onNodeWithText("850 pts").assertIsDisplayed()
    }
}
