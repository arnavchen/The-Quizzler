package com.example.thequizzler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.thequizzler.ui.theme.TheQuizzlerTheme
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.thequizzler.ui.components.BottomNavBar
import com.example.thequizzler.navigation.NavigationHost
import com.example.thequizzler.navigation.Screen
import android.Manifest
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Lifecycle", "MainActivity onCreate")
        enableEdgeToEdge()
        setContent {
            TheQuizzlerTheme {
                Log.d("Lifecycle", "MainActivity Compose setContent")

                val locationPermissionState = rememberPermissionState(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )

                LaunchedEffect(Unit) {
                    if (!locationPermissionState.status.isGranted) {
                        locationPermissionState.launchPermissionRequest()
                    }
                }

                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val showBottomBar = currentRoute in listOf(
                    Screen.Home.route,
                    Screen.Leaderboard.route,
                    Screen.Sessions.route,
                    Screen.Settings.route,
                )

                Scaffold(
                    bottomBar = {
                        if (showBottomBar){
                            BottomNavBar(navController)
                        }

                    }
                ) { innerPadding ->
                    NavigationHost(
                        navController = navController,
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    );
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        Log.d("Lifecycle", "MainActivity onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle", "MainActivity onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle", "MainActivity onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Lifecycle", "MainActivity onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Lifecycle", "MainActivity onDestroy")
    }
}