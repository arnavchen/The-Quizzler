package com.example.thequizzler.ui.screens.quiz

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.thequizzler.quiz.GeneratedQuestion
import com.example.thequizzler.navigation.Screen
import com.example.thequizzler.ui.screens.loading.LoadingHost
import com.example.thequizzler.ui.theme.TheQuizzlerTheme
import com.example.thequizzler.ui.theme.AppSpacing

@Composable
fun MockQuizScreen(navController: NavController, quizViewModel: QuizViewModel) {

    LaunchedEffect(Unit) {
        Log.d("Lifecycle", "MockQuizScreen Composable CREATED");
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Lifecycle", "MockQuizScreen Composable DISPOSED")
        }
    }

    val uiState by quizViewModel.uiState.collectAsState()
    val quizManager = uiState.quizManager

    if (quizManager != null && !quizManager.isFinished) {
        val currentQuestion = quizManager.currentQuestion!!
        var timeLeftMs by remember { mutableStateOf(currentQuestion.timeLimitSeconds * 1000L) }
        val startTime by remember(quizManager.currentQuestionIndex) { mutableStateOf(System.currentTimeMillis()) }

        LaunchedEffect(quizManager.currentQuestionIndex) {
            timeLeftMs = currentQuestion.timeLimitSeconds * 1000L
            val questionStartTime = System.currentTimeMillis()
            while (true) {
                val elapsed = System.currentTimeMillis() - questionStartTime
                val remaining = (currentQuestion.timeLimitSeconds * 1000L) - elapsed
                timeLeftMs = if (remaining > 0) remaining else 0L
                if (timeLeftMs <= 0L) break
                delay(50L)
            }
        }

        val onAnswerSelected: (String) -> Unit = { userAnswer ->
            val timeTaken = System.currentTimeMillis() - startTime
            quizManager.submitAnswer(userAnswer, timeTaken)
            if (quizManager.isFinished) {
                navController.navigate(Screen.Results.route)
            }
        }

        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            HorizontalMockQuizScreen(
                question = currentQuestion,
                questionNumber = quizManager.currentQuestionIndex + 1,
                totalQuestions = quizManager.totalQuestions,
                score = quizManager.score,
                timeLeftSeconds = (timeLeftMs / 1000L).toInt(),
                onAnswer = onAnswerSelected
            )
        } else {
            VerticalMockQuizScreen(
                question = currentQuestion,
                questionNumber = quizManager.currentQuestionIndex + 1,
                totalQuestions = quizManager.totalQuestions,
                score = quizManager.score,
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
            .padding(AppSpacing.large),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Score: $score", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "${timeLeftSeconds}s",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Text("Q$questionNumber/$totalQuestions", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppSpacing.medium),
            tonalElevation = 2.dp,
        ) {
            Text(
                text = question.questionText,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(AppSpacing.medium)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(AppSpacing.medium),
            modifier = Modifier.fillMaxWidth()
        ) {
            var selectedAnswer by remember { mutableStateOf<String?>(null) }
            var selectedIsCorrect by remember { mutableStateOf<Boolean?>(null) }

            question.answers.forEach { answerText ->
                val isSelected = selectedAnswer != null && selectedAnswer == answerText
                val isCorrect = isSelected && (selectedIsCorrect == true)

                AnswerOption(
                    answerText = answerText,
                    isSelected = isSelected,
                    isCorrect = isCorrect,
                    onClick = { chosen ->
                        if (selectedAnswer == null) {
                            selectedAnswer = chosen
                            selectedIsCorrect = question.checkAnswer(chosen)
                        }
                    },
                    enabled = selectedAnswer == null
                )
            }

            // when an answer is selected, submit after a short delay to allow animation
            LaunchedEffect(selectedAnswer) {
                if (selectedAnswer != null) {
                    delay(600L)
                    onAnswer(selectedAnswer!!)
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
        modifier = Modifier
            .fillMaxSize()
            .padding(AppSpacing.large),
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
                verticalArrangement = Arrangement.spacedBy(AppSpacing.medium),
                modifier = Modifier.fillMaxWidth()
            ) {
                var selectedAnswer by remember { mutableStateOf<String?>(null) }
                var selectedIsCorrect by remember { mutableStateOf<Boolean?>(null) }

                question.answers.forEach { answerText ->
                    val isSelected = selectedAnswer != null && selectedAnswer == answerText
                    val isCorrect = isSelected && (selectedIsCorrect == true)

                    AnswerOption(
                        answerText = answerText,
                        isSelected = isSelected,
                        isCorrect = isCorrect,
                        onClick = { chosen ->
                            if (selectedAnswer == null) {
                                selectedAnswer = chosen
                                selectedIsCorrect = question.checkAnswer(chosen)
                            }
                        },
                        enabled = selectedAnswer == null
                    )
                }

                LaunchedEffect(selectedAnswer) {
                    if (selectedAnswer != null) {
                        delay(600L)
                        onAnswer(selectedAnswer!!)
                    }
                }
            }
        }
    }


@Composable
private fun AnswerOption(
    answerText: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    onClick: (String) -> Unit,
    enabled: Boolean = true
) {
    val successColor = Color(0xFF2E7D32)
    val wrongColor = Color(0xFFB00020)
    val defaultColor = MaterialTheme.colorScheme.primary

    val targetColor = when {
        !isSelected -> defaultColor
        isCorrect -> successColor
        else -> wrongColor
    }

    val bgColor by animateColorAsState(targetColor, animationSpec = tween(durationMillis = 300))
    val scaleTarget = if (isSelected && isCorrect) 1.03f else 1f
    val scale by animateFloatAsState(targetValue = scaleTarget, animationSpec = tween(250))

    ElevatedButton(
        onClick = { onClick(answerText) },
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = bgColor,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(answerText, style = MaterialTheme.typography.bodyLarge)
    }
}


// Previews
@Preview(showBackground = true)
@Composable
fun VerticalMockQuizPreview() {
    TheQuizzlerTheme {
        val sampleQuestion = GeneratedQuestion(
            questionText = "What is the capital of France?",
            answers = listOf("Paris", "London", "Berlin", "Madrid"),
            correctAnswer = "Paris",
            checkAnswer = {it == "Paris"},
            timeLimitSeconds = 15
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

@Preview(showBackground = true, widthDp = 800, heightDp = 480)
@Composable
fun HorizontalMockQuizPreview() {
    TheQuizzlerTheme {
        val sampleQuestion = GeneratedQuestion(
            questionText = "Which planet is known as the Red Planet?",
            answers = listOf("Earth", "Mars", "Jupiter", "Venus"),
            correctAnswer = "Mars",
            checkAnswer = {it == "Mars"},
            timeLimitSeconds = 15
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
