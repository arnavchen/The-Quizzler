package com.example.thequizzler

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Leaderboard : Screen("leaderboard_screen")
    object Sessions : Screen("sessions_screen")
    object Settings : Screen("settings_screen")
    object MockQuiz : Screen("mock_quiz/{playerName}")
    object SessionInfo : Screen("sessionInfo/{sessionId}")

    object Results : Screen("results/{score}")
}
