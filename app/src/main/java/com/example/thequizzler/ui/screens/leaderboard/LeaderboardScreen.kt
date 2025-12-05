package com.example.thequizzler.ui.screens.leaderboard

import android.content.res.Configuration
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thequizzler.QuizzlerApplication
import com.example.thequizzler.ui.AppViewModelProvider
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
        val context = LocalContext.current.applicationContext as QuizzlerApplication
        val repository = context.container.highScoresRepository

        val model: LeaderboardViewModel = viewModel(factory = AppViewModelProvider.Factory)

        val sessions by model.highScores.collectAsState()
        val leaderboard = sessions.mapIndexed { index, s -> PlayerScore(s.userName, s.score, index + 1) }

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            HorizontalLeaderboardScreen(leaderboard)
        } else {
            VerticalLeaderboardScreen(leaderboard)

        }
    }
}

@Composable
fun VerticalLeaderboardScreen(leaderboard: List<PlayerScore> = listOf(
    PlayerScore("Arnav", 960, 1),
    PlayerScore("Connor", 952, 2),
    PlayerScore("Amav", 887, 3),
    PlayerScore("Joel", 361, 4),
    PlayerScore("Ian", 67, 5)
)) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Leaderboard",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (leaderboard.isEmpty()) {
            // Empty state
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎯",
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "No scores yet!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Play a quiz to get on the leaderboard",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        } else {
            // Podium for top 3
            if (leaderboard.size >= 3) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // 2nd place
                    PodiumCard(leaderboard[1], Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    // 1st place (taller)
                    PodiumCard(leaderboard[0], Modifier.weight(1f), isFirst = true)
                    Spacer(modifier = Modifier.width(8.dp))
                    // 3rd place
                    PodiumCard(leaderboard[2], Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Divider
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Rest of the leaderboard
            val remainingPlayers = if (leaderboard.size >= 3) leaderboard.drop(3) else leaderboard
            remainingPlayers.forEach { player ->
                LeaderboardCard(player)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun HorizontalLeaderboardScreen(leaderboard: List<PlayerScore> = listOf(
    PlayerScore("Arnav", 960, 1),
    PlayerScore("Connor", 952, 2),
    PlayerScore("Amav", 887, 3),
    PlayerScore("Joel", 361, 4),
    PlayerScore("Ian", 67, 5)
)) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Leaderboard",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (leaderboard.isEmpty()) {
            // Empty state
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎯",
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = "No scores yet!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp)
            ) {
                leaderboard.forEach { player ->
                    HorizontalLeaderboardCard(player, Modifier.width(200.dp))
                }
            }
        }
    }
}

@Composable
fun HorizontalLeaderboardCard(player: PlayerScore, modifier: Modifier = Modifier) {
    val medal = when (player.place) {
        1 -> "🥇"
        2 -> "🥈"
        3 -> "🥉"
        else -> ""
    }

    val borderColor = when (player.place) {
        1 -> Color(0xFFFFD700)
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    }

    Card(
        modifier = modifier
            .height(180.dp)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (player.place <= 3)
                borderColor.copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = borderColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (medal.isNotEmpty()) medal else "#${player.place}",
                    fontSize = if (medal.isNotEmpty()) 24.sp else 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (medal.isEmpty()) Color.Black else Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = player.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${player.score}",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "points",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun PodiumCard(player: PlayerScore, modifier: Modifier = Modifier, isFirst: Boolean = false) {
    val (medal, borderColor) = when (player.place) {
        1 -> Pair("🥇", Color(0xFFFFD700))
        2 -> Pair("🥈", Color(0xFFC0C0C0))
        3 -> Pair("🥉", Color(0xFFCD7F32))
        else -> Pair("", MaterialTheme.colorScheme.outline)
    }

    val cardHeight = if (isFirst) 200.dp else 170.dp

    Card(
        modifier = modifier
            .height(cardHeight)
            .border(3.dp, borderColor, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isFirst) 12.dp else 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = medal,
                fontSize = if (isFirst) 48.sp else 40.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = player.name,
                fontSize = if (isFirst) 20.sp else 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${player.score}",
                fontSize = if (isFirst) 32.sp else 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "points",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun LeaderboardCard(player: PlayerScore, modifier: Modifier = Modifier) {
    val medal = when (player.place) {
        1 -> "🥇"
        2 -> "🥈"
        3 -> "🥉"
        else -> ""
    }

    val borderColor = when (player.place) {
        1 -> Color(0xFFFFD700)
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (player.place <= 3)
                borderColor.copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Rank circle
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = borderColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (medal.isNotEmpty()) medal else "#${player.place}",
                        fontSize = if (medal.isNotEmpty()) 24.sp else 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (medal.isEmpty()) Color.Black else Color.Unspecified
                    )
                }

                Text(
                    text = player.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${player.score}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "points",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VerticalLeaderboardScreenPreview() {
    TheQuizzlerTheme {
        VerticalLeaderboardScreen(
            leaderboard = listOf(
                PlayerScore("Arnav", 960, 1),
                PlayerScore("Connor", 952, 2),
                PlayerScore("Amav", 887, 3),
                PlayerScore("Joel", 361, 4),
                PlayerScore("Ian", 67, 5)
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun HorizontalLeaderboardScreenPreview() {
    TheQuizzlerTheme {
        HorizontalLeaderboardScreen(
            leaderboard = listOf(
                PlayerScore("Arnav", 960, 1),
                PlayerScore("Connor", 952, 2),
                PlayerScore("Amav", 887, 3),
                PlayerScore("Joel", 361, 4),
                PlayerScore("Ian", 67, 5)
            )
        )
    }
}
