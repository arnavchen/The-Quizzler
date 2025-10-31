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
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large back stack of destinations
                        // on the back button as users select items.
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // re-selecting the same item
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
                        restoreState = true
                    }
                },
                label = { Text(screen.route.substring(0, 1).uppercase()) },
                icon = { /* Icons later */ }
            )
        }
    }
// ...

}
