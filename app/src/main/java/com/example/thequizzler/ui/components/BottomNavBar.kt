package com.example.thequizzler.ui.components

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.thequizzler.navigation.Screen

@Composable
fun BottomNavBar(navController: NavHostController) {

    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "BottomNavBar Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "BottomNavBar Composable DISPOSED")
        }
    }

    val items = listOf(
        Screen.Home,
        Screen.Leaderboard,
        Screen.Sessions,
        Screen.Settings
    )
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            val icon = when (screen) {
                Screen.Home -> Icons.Filled.Home
                Screen.Leaderboard -> Icons.Filled.Leaderboard
                Screen.Sessions -> Icons.Filled.ViewAgenda
                Screen.Settings -> Icons.Filled.Settings
                else -> Icons.Filled.Home
            }
            val label = when (screen) {
                Screen.Home -> "Home"
                Screen.Leaderboard -> "Leaderboard"
                Screen.Sessions -> "Sessions"
                Screen.Settings -> "Settings"
                else -> screen.route
            }
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text(label) },
                icon = { Icon(imageVector = icon, contentDescription = label) }
            )
        }
    }

}
