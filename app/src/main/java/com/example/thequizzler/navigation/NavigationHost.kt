package com.example.thequizzler.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.thequizzler.ui.AppViewModelProvider
import com.example.thequizzler.ui.screens.home.HomeScreen
import com.example.thequizzler.ui.screens.leaderboard.LeaderboardScreen
import com.example.thequizzler.ui.screens.loading.LoadingScreen
import com.example.thequizzler.ui.screens.quiz.MockQuizScreen
import com.example.thequizzler.ui.screens.quiz.QuizViewModel
import com.example.thequizzler.ui.screens.quiz.ResultsScreen
import com.example.thequizzler.ui.screens.sessioninfo.SessionInfoScreen
import com.example.thequizzler.ui.screens.sessions.SessionsScreen
import com.example.thequizzler.ui.screens.settings.SettingsScreen


@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    // ... (LaunchedEffect and DisposableEffect are fine)

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
            // You can keep this as is, it's correct
            SessionInfoScreen(navController = navController)
        }

        // REMOVE the old, separate composables for MockQuiz and Results
        // and replace them with this single call to the new graph function.
        quizGraph(navController)
    }
}

// This new function defines the entire quiz flow in a clean, nested graph.
private fun NavGraphBuilder.quizGraph(navController: NavHostController) {
    // Define a new graph with a unique route "quiz_flow"
    navigation(
        startDestination = Screen.Loading.route, // The flow now starts at the loading screen
        route = "quiz_flow"
    ) {
        // 1. The Loading Screen destination
        composable(Screen.Loading.route) { backStackEntry ->
            // Get the parent entry of the GRAPH to share the ViewModel
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("quiz_flow")
            }
            // Create the ViewModel scoped to the graph
            val quizViewModel: QuizViewModel = viewModel(parentEntry, factory = AppViewModelProvider.Factory)

            val playerName = backStackEntry.arguments?.getString("playerName") ?: "Player"
            val uiState by quizViewModel.uiState.collectAsState()

            // This LaunchedEffect triggers the question generation
            LaunchedEffect(Unit) {
                quizViewModel.startQuiz(playerName)
            }

            // This LaunchedEffect listens for when loading is finished
            LaunchedEffect(uiState.isLoading) {
                if (!uiState.isLoading && uiState.quizManager != null) {
                    // Navigate to the quiz screen and remove the loading screen from the back stack
                    navController.navigate(Screen.MockQuiz.route) {
                        popUpTo(Screen.Loading.route) { inclusive = true }
                    }
                }
            }

            LoadingScreen(message = "Generating your personalized quiz...")
        }

        // 2. The Mock Quiz Screen destination
        composable(Screen.MockQuiz.route) { backStackEntry ->
            // Get the SAME shared ViewModel from the graph
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("quiz_flow")
            }
            val quizViewModel: QuizViewModel = viewModel(parentEntry, factory = AppViewModelProvider.Factory)

            // The screen is now much simpler and doesn't need to handle loading
            MockQuizScreen(
                navController = navController,
                quizViewModel = quizViewModel
            )
        }

        // 3. The Results Screen destination
        composable(Screen.Results.route) { backStackEntry ->
            // Get the SAME shared ViewModel from the graph
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("quiz_flow")
            }
            val quizViewModel: QuizViewModel = viewModel(parentEntry, factory = AppViewModelProvider.Factory)

            ResultsScreen(
                navController = navController,
                quizViewModel = quizViewModel
            )
        }
    }
}
