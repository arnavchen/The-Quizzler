
package com.example.thequizzler.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MockQuizScreen(navController: NavController, playerName: String?) {

	LaunchedEffect(Unit) {
		Log.d("Lifecycle", "MockQuizScreen Composable CREATED")
	}

	DisposableEffect(Unit) {
		onDispose {
			Log.d("Lifecycle", "MockQuizScreen Composable DISPOSED")
		}
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(24.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(text = "Mock Quiz")
		Text(text = "Player: ${playerName ?: "Player"}")

		// temporary controls: go back
		Button(onClick = { navController.navigateUp() }, modifier = Modifier.padding(top = 16.dp)) {
			Text("Back")
		}
	}
}

