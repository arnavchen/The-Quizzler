package com.example.thequizzler.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.thequizzler.ui.AppViewModelProvider
import com.example.thequizzler.ui.screens.home.HomeScreen
import com.example.thequizzler.ui.screens.sessioninfo.SessionInfoScreen
import com.example.thequizzler.ui.screens.leaderboard.LeaderboardScreen
import com.example.thequizzler.ui.screens.sessions.SessionsScreen
import com.example.thequizzler.ui.screens.settings.SettingsScreen
import com.example.thequizzler.ui.screens.quiz.ResultsScreen
import com.example.thequizzler.ui.screens.quiz.MockQuizScreen
import com.example.thequizzler.ui.screens.quiz.QuizViewModel


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

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController, modifier = modifier)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }

        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(navController)
        }

        composable(Screen.Sessions.route) {
            SessionsScreen(navController)
        }

        composable(Screen.SessionInfo.route) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getString("sessionId")
            SessionInfoScreen(navController = navController, sessionId = sessionId)
        }

        composable(Screen.MockQuiz.route) { backStackEntry ->
            val playerName = backStackEntry.arguments?.getString("playerName")
            // Get the parent entry for the shared ViewModel
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.MockQuiz.route)
            }
            // Create the shared ViewModel, scoped to the quiz flow
            val quizViewModel: QuizViewModel = viewModel(parentEntry, factory = AppViewModelProvider.Factory)

            MockQuizScreen(
                navController = navController,
                playerName = playerName ?: "Player",
                quizViewModel = quizViewModel
            )
        }

        composable(Screen.Results.route) { backStackEntry ->
            // Get the parent entry for the shared ViewModel
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.MockQuiz.route)
            }
            // Get the SAME shared ViewModel instance
            val quizViewModel: QuizViewModel = viewModel(parentEntry, factory = AppViewModelProvider.Factory)

            ResultsScreen(
                navController = navController,
                quizViewModel = quizViewModel
            )
        }
    }
}
