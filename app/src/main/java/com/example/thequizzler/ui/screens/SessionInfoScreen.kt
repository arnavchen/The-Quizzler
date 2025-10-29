package com.example.thequizzler.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.example.thequizzler.ui.theme.TheQuizzlerTheme

// Placeholder data classes — easy to connect to database later
data class QuestionInfo(
    val questionText: String,
    val userAnswer: String,
    val correctAnswer: String
)

data class SessionInfo(
    val playerName: String,
    val score: String,
    val dateTime: String,
    val questions: List<QuestionInfo>
)

@Composable
fun SessionInfoScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "SessionInfoScreen Composable CREATED")
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "SessionInfoScreen Composable DISPOSED")
        }
    }

    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    // Temporary placeholder session
    val session = SessionInfo(
        playerName = "Arnav",
        dateTime = "9/25/2025, 9:01 PM",
        score = "90",
        questions = listOf(
            QuestionInfo("Which of the following is closest to you?", "McDonald's", "Starbucks"),
            QuestionInfo("How close to you is the nearest National Park?", "20 miles", "10 miles"),
            QuestionInfo("Which state is directly north of Texas?", "Oklahoma", "Oklahoma"),
        )
    )

    if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
        HorizontalSessionInfo(session, navController)
    } else {
        VerticalSessionInfo(session, navController)
    }
}

@Composable
fun VerticalSessionInfo(session: SessionInfo, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Row: Back button + player info
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(session.playerName, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Text(session.dateTime, fontSize = 14.sp, color = Color.Gray)
                }
            }

            // Score in top right
            Text(
                text = "Score: ${session.score}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Question cards
        session.questions.forEach { question ->
            QuestionCard(question = question, onClick = {
                // navigate to detail later
                // navController.navigate("questionDetail/${question.id}")
            })
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun HorizontalSessionInfo(session: SessionInfo, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(session.playerName, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Text(session.dateTime, fontSize = 14.sp, color = Color.Gray)
                }
            }

            // Score in top right
            Text(
                text = "Score: ${session.score}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Horizontal scroll row of question cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            session.questions.forEach { question ->
                QuestionCard(
                    question = question,
                    onClick = { /* Navigate to detailed question later */ },
                    modifier = Modifier.width(220.dp)
                )
            }
        }
    }
}

@Composable
fun QuestionCard(
    question: QuestionInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = question.questionText,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text("You said:", fontWeight = FontWeight.Medium)
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .padding(6.dp)
                    ) {
                        Text(question.userAnswer)
                    }
                }
                Column(horizontalAlignment = Alignment.Start) {
                    Text("Correct Answer:", fontWeight = FontWeight.Medium)
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .padding(6.dp)
                    ) {
                        Text(question.correctAnswer)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SessionInfoScreenPreview() {
    TheQuizzlerTheme {
        SessionInfoScreen(navController = rememberNavController())
    }
}
