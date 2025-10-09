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
fun SessionsScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "SessionsScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "SessionsScreen Composable DISPOSED")
        }
    }

    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
        HorizontalSessionsScreen()
    } else {
        VerticalSessionsScreen()
    }
}

@Composable
fun VerticalSessionsScreen() {
    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "VerticalSessionsScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "VerticalSessionsScreen Composable DISPOSED")
        }
    }
}

@Composable
fun HorizontalSessionsScreen() {
    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "HorizontalSessionsScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "HorizontalSessionsScreen Composable DISPOSED")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SessionsScreenPreview() {
    TheQuizzlerTheme {
        SessionsScreen(navController = rememberNavController())
    }
}