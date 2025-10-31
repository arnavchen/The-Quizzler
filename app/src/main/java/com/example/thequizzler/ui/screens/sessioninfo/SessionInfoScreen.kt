package com.example.thequizzler.ui.screens.sessioninfo

import android.content.res.Configuration
import android.util.Log
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thequizzler.dataPersistence.models.QuestionInstance
import com.example.thequizzler.dataPersistence.models.Session
import com.example.thequizzler.ui.AppViewModelProvider
import com.example.thequizzler.ui.theme.TheQuizzlerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.text.format

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
                    Text(session.userName, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Text(
                        SimpleDateFormat("M/d/yyyy, h:mm a", Locale.getDefault()).format(Date(session.startTime)),
                        fontSize = 14.sp, color = Color.Gray
                    )
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
        questions.forEach { question ->
            QuestionCard(question = question, onClick = {
                // navigate to detail later
                // navController.navigate("questionDetail/${question.id}")
            })
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun HorizontalSessionInfo(session: Session, questions: List<QuestionInstance>, navController: NavController) {
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
                    Text(session.userName, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Text(
                        SimpleDateFormat("M/d/yyyy, h:mm a", Locale.getDefault()).format(Date(session.startTime)),
                        fontSize = 14.sp, color = Color.Gray
                    )
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
            questions.forEach { question ->
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
    question: QuestionInstance,
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
                text = question.question,
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
                        Text(question.userResponse)
                    }
                }
                Column(horizontalAlignment = Alignment.Start) {
                    Text("Correct Answer:", fontWeight = FontWeight.Medium)
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .padding(6.dp)
                    ) {
                        Text(question.correctResponse)
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
