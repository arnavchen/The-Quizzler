package com.example.thequizzler.ui.screens.sessioninfo

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thequizzler.dataPersistence.models.QuestionInstance
import com.example.thequizzler.dataPersistence.models.Session
import com.example.thequizzler.ui.AppViewModelProvider
import com.example.thequizzler.ui.theme.TheQuizzlerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SessionInfoScreen(navController: NavController, sessionId: String? = null) {
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

    val viewModel: SessionInfoViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val uiState by viewModel.uiState.collectAsState()

    if(uiState.session != null) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            HorizontalSessionInfo(uiState.session!!, questions=uiState.questions, navController)
        } else {
            VerticalSessionInfo(uiState.session!!, questions=uiState.questions, navController)
        }
    }
}

@Composable
fun VerticalSessionInfo(session: Session, questions: List<QuestionInstance>, navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize() ,
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            SessionHeader(session, navController)
        }

        items(questions) { question ->
            QuestionCard(question = question, onClick = {}, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun HorizontalSessionInfo(session: Session, questions: List<QuestionInstance>, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SessionHeader(session, navController, modifier = Modifier.padding(16.dp))

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(questions) { question ->
                QuestionCard(
                    question = question,
                    onClick = {},
                    modifier = Modifier.width(280.dp)
                )
            }
            }
    }
}

@Composable
private fun SessionHeader(
    session: Session,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Back button
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .size(44.dp)
                .align(Alignment.Start)
                .semantics {
                    contentDescription = "Back to sessions list"
                }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }

        // Player info and score row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    session.userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.semantics {
                        heading()
                    }
                )
                Text(
                    SimpleDateFormat("M/d/yyyy, h:mm a", Locale.getDefault()).format(Date(session.startTime)),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Score display
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.semantics(mergeDescendants = true) {
                    contentDescription = "Total score: ${session.score} points"
                }
            ) {
                Text(
                    "${session.score}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Score",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Composable
fun QuestionCard(
    question: QuestionInstance,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCorrect = question.userResponse.equals(question.correctResponse, ignoreCase = true)
    val resultText = if (isCorrect) "Correct" else "Incorrect"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .semantics(mergeDescendants = true) {
                contentDescription = "$resultText. Question: ${question.question}. Your answer: ${question.userResponse}. Correct answer: ${question.correctResponse}"
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            2.dp,
            if (isCorrect) Color(0xFF4CAF50) else Color(0xFFf44336)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header with status indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question.question,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (isCorrect) Icons.Filled.Check else Icons.Filled.Close,
                    contentDescription = if (isCorrect) "Correct" else "Incorrect",
                    tint = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFf44336),
                    modifier = Modifier.size(24.dp)
                )
            }

            // User's response
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "Your Answer:",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    question.userResponse,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Correct answer
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFF4CAF50).copy(alpha = 0.15f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "Correct Answer:",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    question.correctResponse,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1B5E20)
                )
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
