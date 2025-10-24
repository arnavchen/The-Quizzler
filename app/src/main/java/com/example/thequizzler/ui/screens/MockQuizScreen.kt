package com.example.thequizzler.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thequizzler.ui.theme.TheQuizzlerTheme

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

    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    val questions = listOf(
        "Which of the following is closest to you?",
        "What color is the sky?",
        "Which is a fruit?",
        "What’s 2 + 2?",
        "Which state is north of Texas?",
        "Which animal barks?",
        "What’s the capital of France?",
        "What’s H2O?",
        "Which of these is a programming language?",
        "What planet do we live on?"
    )

    // state for question index and score
    var questionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }

    if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
        HorizontalMockQuizScreen(
            playerName = playerName,
            questions = questions,
            questionIndex = questionIndex,
            score = score,
            onAnswer = {
                score += 10
                if (questionIndex < questions.lastIndex) {
                    questionIndex++
                } else {
                    navController.navigate("results/$score")
                }
            },
            onBack = { navController.navigateUp() }
        )
    } else {
        VerticalMockQuizScreen(
            playerName = playerName,
            questions = questions,
            questionIndex = questionIndex,
            score = score,
            onAnswer = {
                score += 10
                if (questionIndex < questions.lastIndex) {
                    questionIndex++
                } else {
                    navController.navigate("results/$score")
                }
            },
            onBack = { navController.navigateUp() }
        )
    }
}

@Composable
fun VerticalMockQuizScreen(
    playerName: String?,
    questions: List<String>,
    questionIndex: Int,
    score: Int,
    onAnswer: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Score: $score", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text("Q${questionIndex + 1}/10", color = MaterialTheme.colorScheme.primary)
        Text(
            text = questions[questionIndex],
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            repeat(4) { i ->
                Button(onClick = { onAnswer() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Option ${i + 1}")
                }
            }
        }

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}

@Composable
fun HorizontalMockQuizScreen(
    playerName: String?,
    questions: List<String>,
    questionIndex: Int,
    score: Int,
    onAnswer: () -> Unit,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Score: $score", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("Q${questionIndex + 1}/10", color = MaterialTheme.colorScheme.primary)
            Text(
                text = questions[questionIndex],
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            repeat(4) { i ->
                Button(onClick = { onAnswer() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Option ${i + 1}")
                }
            }

            Button(onClick = onBack) {
                Text("Back")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VerticalMockQuizPreview() {
    TheQuizzlerTheme {
        val navController = rememberNavController()
        VerticalMockQuizScreen(
            playerName = "Arnav",
            questions = List(10) { "Question ${it + 1}" },
            questionIndex = 0,
            score = 50,
            onAnswer = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun HorizontalMockQuizPreview() {
    TheQuizzlerTheme {
        val navController = rememberNavController()
        HorizontalMockQuizScreen(
            playerName = "Arnav",
            questions = List(10) { "Question ${it + 1}" },
            questionIndex = 1,
            score = 70,
            onAnswer = {},
            onBack = {}
        )
    }
}
