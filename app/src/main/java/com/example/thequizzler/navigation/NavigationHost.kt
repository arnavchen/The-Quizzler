package com.example.thequizzler.navigation

import android.util.Log
import androidx.compose.runtime.Composable;
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.thequizzler.ui.screens.*

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {

    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "Navigation Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "Navigation Composable DISPOSED")
        }
    }

    NavHost(navController = navController, startDestination = Screen.Home.route, modifier = modifier) {
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController) }
        composable(Screen.Leaderboard.route) { LeaderboardScreen(navController) }
        composable(Screen.Sessions.route) { SessionsScreen(navController) }
    }
}
