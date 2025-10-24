package com.example.thequizzler.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.example.thequizzler.ui.theme.TheQuizzlerTheme

// Temporary data class for placeholder sessions
data class Session(
    val id: Int,
    val playerName: String,
    val score: Int,
    val correct: Int,
    val incorrect: Int,
    val rank: String
)

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

    // Subtle gradient background
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
            HorizontalSessionsScreen(navController)
        } else {
            VerticalSessionsScreen(navController)
        }
    }
}

@Composable
fun VerticalSessionsScreen(navController: NavController) {
    val sessions = listOf(
        Session(1, "Arnav", 960, 9, 1, "1st"),
        Session(2, "Connor", 952, 8, 2, "2nd"),
        Session(3, "Ian", 67, 1, 9, "3rd"),
        Session(4, "Joel", 361, 5, 5, "4th")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Past Sessions",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        sessions.forEach { session ->
            SessionCard(session, navController)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun HorizontalSessionsScreen(navController: NavController) {
    val sessions = listOf(
        Session(1, "Arnav", 960, 9, 1, "1st"),
        Session(2, "Connor", 952, 8, 2, "2nd"),
        Session(3, "Ian", 67, 1, 9, "3rd"),
        Session(4, "Joel", 361, 5, 5, "4th")
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Past Sessions",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            sessions.forEach { session ->
                SessionCard(session, navController, Modifier.width(220.dp))
            }
        }
    }
}

@Composable
fun SessionCard(session: Session, navController: NavController, modifier: Modifier = Modifier) {
    val rankColor = when (session.rank) {
        "1st" -> Color(0xFFFFD700) // Gold
        "2nd" -> Color(0xFFC0C0C0) // Silver
        "3rd" -> Color(0xFFCD7F32) // Bronze
        else -> MaterialTheme.colorScheme.tertiary
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                // 👇 Navigate to the session info screen
                navController.navigate("sessionInfo/${session.id}")
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = session.playerName,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "${session.score} pts",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${session.correct} correct",
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${session.incorrect} wrong",
                    color = Color(0xFFC62828),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = session.rank,
                color = rankColor,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
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
