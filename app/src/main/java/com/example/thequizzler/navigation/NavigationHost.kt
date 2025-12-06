package com.example.thequizzler.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import android.Manifest
import android.content.Context
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import com.example.thequizzler.quiz.templates.SimpleLocation
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
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
import com.example.thequizzler.ui.screens.quiz.QuizScreen
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
            SessionInfoScreen(navController = navController)
        }

        quizGraph(navController)
    }
}

private suspend fun getLastKnownSimpleLocation(context: Context): SimpleLocation? = suspendCancellableCoroutine { cont ->
    val fused = LocationServices.getFusedLocationProviderClient(context)
    try {
        fused.lastLocation.addOnSuccessListener { loc ->
            cont.resume(loc?.let { SimpleLocation(it.latitude, it.longitude) })
        }.addOnFailureListener {
            cont.resume(null)
        }
    } catch (e: SecurityException) {
        cont.resume(null)
    }
}

private fun NavGraphBuilder.quizGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.Loading.route, // The flow starts at the loading screen
        route = "quiz_flow/{playerName}"
    ) {
        // 1. The Loading Screen destination
        composable(Screen.Loading.route) { backStackEntry ->
            // Get the parent entry of the GRAPH to share the ViewModel
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("quiz_flow/{playerName}")
            }
            // Create the ViewModel scoped to the graph
            val quizViewModel: QuizViewModel = viewModel(parentEntry, factory = AppViewModelProvider.Factory)

            val playerName = backStackEntry.arguments?.getString("playerName") ?: "Player"
            val uiState by quizViewModel.uiState.collectAsState()

            // Provide a Context from the composable scope
            val context = LocalContext.current

            // This LaunchedEffect triggers the question generation
            LaunchedEffect(Unit) {
                val simpleLocation: SimpleLocation? = if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    try {
                        getLastKnownSimpleLocation(context)
                    } catch (e: Exception) {
                        null
                    }
                } else null

                // If we couldn't obtain a location but the user had location enabled,
                // disable the location setting to avoid errors
                quizViewModel.disableLocationPreferenceIfUnavailable(simpleLocation)

                quizViewModel.startQuiz(playerName, simpleLocation)
            }

            // This LaunchedEffect listens for when loading is finished
            LaunchedEffect(uiState.isLoading, uiState.generationFailed, uiState.quizManager) {
                if (!uiState.isLoading && uiState.quizManager != null) {
                    // Navigate to the quiz screen and remove the loading screen from the back stack
                    navController.navigate(Screen.MockQuiz.route) {
                        popUpTo(Screen.Loading.route) { inclusive = true }
                    }
                }
            }

            LoadingScreen(
                message = "Generating your personalized quiz...",
                generationFailed = uiState.generationFailed,
                onReturnHome = {
                    // Navigate back to the home screen
                    navController.navigate(Screen.Home.route) {
                        // Clear the entire back stack up to Home
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. The Mock Quiz Screen destination
        composable(Screen.MockQuiz.route) { backStackEntry ->
            // Get the shared ViewModel from the graph
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("quiz_flow/{playerName}")
            }
            val quizViewModel: QuizViewModel = viewModel(parentEntry, factory = AppViewModelProvider.Factory)

            QuizScreen(
                navController = navController,
                quizViewModel = quizViewModel
            )
        }

        // 3. The Results Screen destination
        composable(Screen.Results.route) { backStackEntry ->
            // Get the shared ViewModel from the graph
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("quiz_flow/{playerName}")
            }
            val quizViewModel: QuizViewModel = viewModel(parentEntry, factory = AppViewModelProvider.Factory)

            ResultsScreen(
                navController = navController,
                quizViewModel = quizViewModel
            )
        }
    }
}
