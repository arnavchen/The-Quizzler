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
fun SettingsScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "SettingsScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "SettingsScreen Composable DISPOSED")
        }
    }

    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
        HorizontalSettingsScreen()
    } else {
        VerticalSettingsScreen()
    }
}

@Composable
fun VerticalSettingsScreen() {
    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "VerticalSettingsScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "VerticalSettingsScreen Composable DISPOSED")
        }
    }
}

@Composable
fun HorizontalSettingsScreen() {
    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "HorizontalSettingsScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "HorizontalSettingsScreen Composable DISPOSED")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    TheQuizzlerTheme {
        SettingsScreen(navController = rememberNavController())
    }
}