package com.example.thequizzler.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thequizzler.ui.theme.TheQuizzlerTheme

@Composable
fun LeaderboardScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "LeaderboardScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "LeaderboardScreen Composable DISPOSED")
        }
    }

    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
        HorizontalLeaderboardScreen()
    } else {
        VerticalLeaderboardScreen()
    }
}

@Composable
fun VerticalLeaderboardScreen() {
    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "VerticalLeaderboardScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "VerticalLeaderboardScreen Composable DISPOSED")
        }
    }
}

@Composable
fun HorizontalLeaderboardScreen() {
    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "HorizontalLeaderboardScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "HorizontalLeaderboardScreen Composable DISPOSED")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardScreenPreview() {
    TheQuizzlerTheme {
        LeaderboardScreen(navController = rememberNavController())
    }
}