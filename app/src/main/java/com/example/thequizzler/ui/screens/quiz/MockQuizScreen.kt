package com.example.thequizzler.ui.screens.quiz

import android.content.res.Configuration
import android.provider.Contacts
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thequizzler.navigation.Screen
import com.example.thequizzler.ui.theme.TheQuizzlerTheme

@Composable
fun MockQuizScreen(navController: NavController, playerName: String?, quizViewModel: QuizViewModel) {

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

    val mockQuestions = remember {
        listOf(
            GeneratedQuestion("What is 2+2?", listOf("3", "4", "5", "6"), "4"),
            GeneratedQuestion("What color is the sky?", listOf("Blue", "Green", "Red", "Yellow"), "Blue"),
            GeneratedQuestion("Which planet is known as the Red Planet?", listOf("Earth", "Mars", "Jupiter", "Venus"), "Mars"),
            GeneratedQuestion("Which of the following is closest to you?", listOf("Your Laptop", "Living Mammoth", "The Moon", "Reaching Your Hopes and Dreams"), "Your Laptop"),
            GeneratedQuestion("What is the capital of France?", listOf("Paris", "London", "Berlin", "Madrid"), "Paris"),
            GeneratedQuestion("What is the largest mammal?", listOf("Elephant", "Blue Whale", "Giraffe", "Hippopotamus"), "Blue Whale"),
            GeneratedQuestion("What State are you in?", listOf("Texas", "California", "New York", "Despair"), "Despair"),
            GeneratedQuestion("Which is a fruit?", listOf("Carrot", "Cherry", "Drywall", "Loops"), "Cherry"),
            GeneratedQuestion("What is H2O?", listOf("Water", "Earth", "Fire", "Air"), "Water"),
            GeneratedQuestion("Did you enjoy this quiz?", listOf("Yes", "No", "Absolutely Not", "End my Suffering"), "Yes")
        )
    }

    LaunchedEffect(Unit) {
        quizViewModel.startQuiz(playerName ?: "Player", mockQuestions)
    }

    val quizState by quizViewModel.quizState.collectAsState()
    var questionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }

    val totalTimeSeconds = 15.0 // default total seconds per question
    val totalTimeMs = (totalTimeSeconds * 1000).toLong()

    var timeLeftMs by remember { mutableStateOf(totalTimeMs) }

    LaunchedEffect(questionIndex) {
        val startTime = System.currentTimeMillis()
        // reset timeLeft
        timeLeftMs = totalTimeMs
        while (true) {
            val elapsed = System.currentTimeMillis() - startTime
            val remaining = totalTimeMs - elapsed
            timeLeftMs = if (remaining > 0) remaining else 0L
            if (timeLeftMs <= 0L) break
            delay(50L)
        }
    }

    val onAnswerSelected: (String) -> Unit = { userAnswer ->
        val currentQuestion = mockQuestions[questionIndex]
        val wasCorrect = userAnswer == currentQuestion.correctAnswer
        val points = if (wasCorrect) {
            (100.0 * (timeLeftMs.toDouble() / totalTimeMs.toDouble())).roundToInt()
        } else {
            0
        }

        // Record the answer into the viewModel's questionInstance record
        quizViewModel.recordAnswer(
            questionNumber = questionIndex + 1,
            questionText = currentQuestion.questionText,
            userAnswer = userAnswer,
            correctAnswer = currentQuestion.correctAnswer,
            wasCorrect = wasCorrect,
            pointsAwarded = points
        )

        if (questionIndex < mockQuestions.lastIndex) {
            questionIndex++
        } else {
            navController.navigate(Screen.Results.route)
        }
    }

    if (quizState.generatedQuestions.isNotEmpty() && questionIndex < quizState.generatedQuestions.size) {
        val currentGeneratedQuestion = quizState.generatedQuestions[questionIndex]

        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            HorizontalMockQuizScreen(
                question = currentGeneratedQuestion,
                questionNumber = questionIndex + 1,
                totalQuestions = quizState.generatedQuestions.size,
                score = quizState.score,
                timeLeftSeconds = (timeLeftMs / 1000L).toInt(),
                onAnswer = onAnswerSelected
            )
        } else {
            VerticalMockQuizScreen(
                question = currentGeneratedQuestion,
                questionNumber = questionIndex + 1,
                totalQuestions = quizState.generatedQuestions.size,
                score = quizState.score,
                timeLeftSeconds = (timeLeftMs / 1000L).toInt(),
                onAnswer = onAnswerSelected
            )
        }
    }
}

@Composable
fun VerticalMockQuizScreen(
    question: GeneratedQuestion,
    questionNumber: Int,
    totalQuestions: Int,
    score: Int,
    timeLeftSeconds: Int,
    onAnswer: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Score: $score", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(
                text = "${timeLeftSeconds}s",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.secondary
            )
            Text("Q$questionNumber/$totalQuestions", color = MaterialTheme.colorScheme.primary)
        }

        Text(
            text = question.questionText,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            question.answers.forEach { answerText ->
                Button(onClick = { onAnswer(answerText) }, modifier = Modifier.fillMaxWidth()) {
                    Text(answerText)
                }
            }
        }
    }
}

@Composable
fun HorizontalMockQuizScreen(
    question: GeneratedQuestion,
    questionNumber: Int,
    totalQuestions: Int,
    score: Int,
    timeLeftSeconds: Int,
    onAnswer: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = question.questionText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Score: $score", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(
                    text = "${timeLeftSeconds}s",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text("Q$questionNumber/$totalQuestions", color = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.height(24.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                question.answers.forEach { answerText ->
                    Button(onClick = { onAnswer(answerText) }, modifier = Modifier.fillMaxWidth()) {
                        Text(answerText)
                    }
                }
            }
        }
    }
}


// In: app/src/main/java/com/example/thequizzler/ui/screens/quiz/MockQuizScreen.kt

@Preview(showBackground = true)
@Composable
fun VerticalMockQuizPreview() {
    TheQuizzlerTheme {
        val sampleQuestion = GeneratedQuestion(
            questionText = "What is the capital of France?",
            answers = listOf("Paris", "London", "Berlin", "Madrid"),
            correctAnswer = "Paris"
        )
        VerticalMockQuizScreen(
            question = sampleQuestion,
            questionNumber = 1,
            totalQuestions = 10,
            score = 50,
            timeLeftSeconds = 10,
            onAnswer = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 480) // Adjusted size for landscape
@Composable
fun HorizontalMockQuizPreview() {
    TheQuizzlerTheme {
        val sampleQuestion = GeneratedQuestion(
            questionText = "Which planet is known as the Red Planet?",
            answers = listOf("Earth", "Mars", "Jupiter", "Venus"),
            correctAnswer = "Mars"
        )
        HorizontalMockQuizScreen(
            question = sampleQuestion,
            questionNumber = 2,
            totalQuestions = 10,
            score = 70,
            timeLeftSeconds = 10,
            onAnswer = {}
        )
    }
}

