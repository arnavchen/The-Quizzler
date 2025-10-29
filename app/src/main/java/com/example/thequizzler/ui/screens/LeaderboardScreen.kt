package com.example.thequizzler.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thequizzler.ui.theme.TheQuizzlerTheme

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

    // Soft gradient background to match SessionsScreen
    val gradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            MaterialTheme.colorScheme.surface
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            HorizontalLeaderboardScreen()
        } else {
            VerticalLeaderboardScreen()
        }
    }
}

@Composable
fun VerticalLeaderboardScreen() {
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
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Leaderboard",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        leaderboard.forEach { player ->
            LeaderboardCard(player)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun HorizontalLeaderboardScreen() {
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
            .padding(vertical = 16.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Leaderboard",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            leaderboard.forEach { player ->
                LeaderboardCard(player, Modifier.width(220.dp))
            }
        }
    }
}

@Composable
fun LeaderboardCard(player: PlayerScore, modifier: Modifier = Modifier) {
    val borderColor = when (player.place) {
        1 -> Color(0xFFFFD700)
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(2.dp, borderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${player.place} ${if (player.place == 1) "🏆" else ""}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = borderColor
            )

            Text(
                text = player.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "${player.score} pts",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
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
