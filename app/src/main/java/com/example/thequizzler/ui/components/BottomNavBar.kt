package com.example.thequizzler.ui.components

import android.util.Log
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
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = { navController.navigate(screen.route) },
                label = { Text(screen.route[0].uppercaseChar().toString()) },
                icon = { /* Replace labels with icons later */ }
            )
        }
    }
}
