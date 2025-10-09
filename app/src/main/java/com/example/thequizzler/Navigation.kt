package com.example.thequizzler

import android.util.Log
import androidx.compose.runtime.Composable;
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.thequizzler.Screen;
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.thequizzler.ui.screens.*

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {

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
