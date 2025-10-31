package com.example.thequizzler.ui.screens.sessions

import android.content.res.Configuration
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thequizzler.dataPersistence.models.Session
import com.example.thequizzler.ui.AppViewModelProvider
import com.example.thequizzler.ui.screens.settings.SettingsViewModel
import com.example.thequizzler.ui.theme.TheQuizzlerTheme

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

    val model: SessionsViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val sessionsUiState by model.sessionsUiState.collectAsState()

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
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            HorizontalSessionsScreen(navController, sessionsUiState.sessionList)
        } else {
            VerticalSessionsScreen(navController, sessionsUiState.sessionList)
        }
    }
}

@Composable
fun VerticalSessionsScreen(navController: NavController, sessions: List<Session>) {

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
fun HorizontalSessionsScreen(navController: NavController, sessions: List<Session>) {

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

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
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
                text = session.userName,
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
                    text = "${session.numCorrect} correct",
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${10 - session.numCorrect} wrong",
                    color = Color(0xFFC62828),
                    fontWeight = FontWeight.SemiBold
                )
            }

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
