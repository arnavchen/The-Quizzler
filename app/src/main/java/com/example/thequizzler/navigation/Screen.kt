package com.example.thequizzler.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home_screen")
    object Leaderboard: Screen("leaderboard_screen")
    object Sessions: Screen("sessions_screen")
    object Settings: Screen("settings_screen")
}