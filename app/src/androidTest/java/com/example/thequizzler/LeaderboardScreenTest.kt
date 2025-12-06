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

    // A fake list of scores to use for tests.
    private val fakeLeaderboardData = listOf(
        PlayerScore("Alice", 980, 1),
        PlayerScore("Bob", 850, 2),
        PlayerScore("Charlie", 720, 3),
        PlayerScore("David", 650, 4)
    )

    @Test
    fun leaderboardTitle_isDisplayed() {
        // Create a vertical leaderboard with the fake list
        composeTestRule.setContent {
            TheQuizzlerTheme {
                VerticalLeaderboardScreen(leaderboard = emptyList())
            }
        }

        // Verify the title "Leaderboard" is on the screen
        composeTestRule.onNodeWithText("Leaderboard").assertIsDisplayed()
    }

    @Test
    fun emptyLeaderboard_showsPlaceholder() {
        // Test an empty list
        composeTestRule.setContent {
            TheQuizzlerTheme {
                VerticalLeaderboardScreen(leaderboard = emptyList())
            }
        }

        // Verify the placeholder text is visible when the leaderboard is empty
        composeTestRule.onNodeWithText("No scores yet!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Play a quiz to get on the leaderboard").assertIsDisplayed()
    }

    @Test
    fun podiumPlayers_areDisplayedCorrectly() {

        composeTestRule.setContent {
            TheQuizzlerTheme {
                VerticalLeaderboardScreen(leaderboard = fakeLeaderboardData)
            }
        }

        // Verify the top 3 players are displayed correctly
        composeTestRule.onNodeWithText("Alice").assertIsDisplayed()
        composeTestRule.onNodeWithText("980").assertIsDisplayed() // Score is now separate from "pts"

        composeTestRule.onNodeWithText("Bob").assertIsDisplayed()
        composeTestRule.onNodeWithText("850").assertIsDisplayed()

        composeTestRule.onNodeWithText("Charlie").assertIsDisplayed()
        composeTestRule.onNodeWithText("720").assertIsDisplayed()
    }

    @Test
    fun remainingPlayers_areDisplayedInList() {

        composeTestRule.setContent {
            TheQuizzlerTheme {
                VerticalLeaderboardScreen(leaderboard = fakeLeaderboardData)
            }
        }

        // Verify the 4th place player is correctly displayed
        composeTestRule.onNodeWithText("David").assertIsDisplayed()

        composeTestRule.onNodeWithText("650").assertIsDisplayed()
    }

    @Test
    fun podiumPlayers_haveMedalEmojis() {
        composeTestRule.setContent {
            TheQuizzlerTheme {
                VerticalLeaderboardScreen(leaderboard = fakeLeaderboardData)
            }
        }

        // Check for the specific medal emojis which are now used in the podium
        composeTestRule.onNodeWithText("🥇").assertIsDisplayed() // 1st place
        composeTestRule.onNodeWithText("🥈").assertIsDisplayed() // 2nd place
        composeTestRule.onNodeWithText("🥉").assertIsDisplayed() // 3rd place
    }
}
