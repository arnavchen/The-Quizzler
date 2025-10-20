package com.example.thequizzler.ui.screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.thequizzler.ui.theme.TheQuizzlerTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

data class PlayerScore(
    val name: String,
    val score: Int,
    val place: Int
)

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

    //val leaderboard by leaderboardViewModel.leaderboard.collectAsState()
    // Temporary placeholder data until database integration
    val leaderboard = listOf(
        PlayerScore("Arnav", 960, 1),
        PlayerScore("Connor", 952, 2),
        PlayerScore("Amav", 887, 3),
        PlayerScore("Joel", 361, 4),
        PlayerScore("Ian", 67, 5)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Leaderboard",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Name", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("Score", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("Place", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        }

        // Leaderboard items
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(leaderboard) { _, player ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(0.5.dp, Color.LightGray)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(player.name, modifier = Modifier.weight(1f))
                    Text(player.score.toString(), modifier = Modifier.weight(1f))
                    Text(player.place.toString(), modifier = Modifier.weight(1f))
                }
            }
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

    val leaderboard = listOf(
        PlayerScore("Arnav", 960, 1),
        PlayerScore("Connor", 952, 2),
        PlayerScore("Amav", 887, 3),
        PlayerScore("Joel", 361, 4),
        PlayerScore("Ian", 67, 5)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Leaderboard",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Horizontal table
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Place", fontWeight = FontWeight.Bold)
                leaderboard.forEach { Text(it.place.toString()) }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Score", fontWeight = FontWeight.Bold)
                leaderboard.forEach { Text(it.score.toString()) }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Name", fontWeight = FontWeight.Bold)
                leaderboard.forEach { Text(it.name) }
            }
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
