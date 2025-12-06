package com.example.thequizzler.ui.screens.quiz

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.thequizzler.navigation.Screen
import com.example.thequizzler.quiz.GeneratedQuestion
import com.example.thequizzler.ui.theme.AppSpacing
import com.example.thequizzler.ui.theme.TheQuizzlerTheme
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(navController: NavController, quizViewModel: QuizViewModel) {

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
    val isAnswerRevealed = uiState.isAnswerRevealed

    if (quizManager != null && !quizManager.isFinished) {
        val currentQuestion = quizManager.currentQuestion!!
        var timeLeftMs by remember { mutableStateOf(currentQuestion.timeLimitSeconds * 1000L) }
        val startTime by remember(quizManager.currentQuestionIndex) { mutableStateOf(System.currentTimeMillis()) }

        val onAnswerSelected: (String) -> Unit = { userAnswer ->
            if(!isAnswerRevealed) {
                val timeTaken = System.currentTimeMillis() - startTime
                quizViewModel.submitAnswer(userAnswer, timeTaken);
            }
        }

        val onNextClicked = {
            quizViewModel.nextQuestion()
            // Check if the quiz is finished after advancing question
            if (quizManager.isFinished) {
                navController.navigate(Screen.Results.route) {
                    popUpTo(Screen.MockQuiz.route) { inclusive = true }
                }
            }
        }

        LaunchedEffect(quizManager.currentQuestionIndex, isAnswerRevealed) {
            if(!isAnswerRevealed) {
                timeLeftMs = currentQuestion.timeLimitSeconds * 1000L
                val questionStartTime = System.currentTimeMillis()
                while (true) {
                    val elapsed = System.currentTimeMillis() - questionStartTime
                    val remaining = (currentQuestion.timeLimitSeconds * 1000L) - elapsed
                    timeLeftMs = if (remaining > 0) remaining else 0L
                    if (timeLeftMs <= 0L) {
                        quizViewModel.submitAnswer("", elapsed)
                        break
                    }
                    delay(50L)
                }
            }
        }

        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            HorizontalQuizScreen(
                question = currentQuestion,
                questionNumber = quizManager.currentQuestionIndex + 1,
                totalQuestions = quizManager.totalQuestions,
                score = quizManager.score,
                timeLeftSeconds = (timeLeftMs / 1000L).toInt(),
                onAnswer = onAnswerSelected,
                isAnswerRevealed = isAnswerRevealed,
                onNext = onNextClicked
            )
        } else {
            VerticalQuizScreen(
                question = currentQuestion,
                questionNumber = quizManager.currentQuestionIndex + 1,
                totalQuestions = quizManager.totalQuestions,
                score = quizManager.score,
                timeLeftSeconds = (timeLeftMs / 1000L).toInt(),
                onAnswer = onAnswerSelected,
                isAnswerRevealed = isAnswerRevealed,
                onNext = onNextClicked
            )
        }
    }
}

@Composable
fun VerticalQuizScreen(
    question: GeneratedQuestion,
    questionNumber: Int,
    totalQuestions: Int,
    score: Int,
    timeLeftSeconds: Int,
    onAnswer: (String) -> Unit,
    isAnswerRevealed: Boolean,
    onNext: () -> Unit
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
            Text(
                "Score: $score",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.semantics {
                    contentDescription = "Current score is $score points"
                    liveRegion = LiveRegionMode.Polite
                }
            )
            Text(
                text = "${timeLeftSeconds}s",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.semantics {
                    liveRegion = LiveRegionMode.Assertive
                }
            )
            Text(
                "Q$questionNumber/$totalQuestions",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.semantics {
                    contentDescription = "Question $questionNumber of $totalQuestions"
                }
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppSpacing.medium)
                .semantics {
                    contentDescription = "Question: ${question.questionText}"
                },
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
            var selectedAnswer by remember(question) { mutableStateOf<String?>(null) }

            LaunchedEffect(selectedAnswer) {
                if (selectedAnswer != null) {
                    onAnswer(selectedAnswer!!)
                }
            }

            question.answers.forEach { answerText ->
                AnswerOption(
                    answerText = answerText,
                    isSelected = selectedAnswer == answerText,
                    isCorrect = isAnswerRevealed && question.checkAnswer(answerText),
                    isActuallyCorrect = question.correctAnswer == answerText,
                    isRevealed = isAnswerRevealed,
                    onClick = { chosen ->
                        if (selectedAnswer == null) {
                            selectedAnswer = chosen
                        }
                    },
                    enabled = !isAnswerRevealed
                )
            }

            if (isAnswerRevealed) {
                Spacer(Modifier.height(AppSpacing.medium))
                Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
                    Text("Next")
                }
            }
        }
    }
}

@Composable
fun HorizontalQuizScreen(
    question: GeneratedQuestion,
    questionNumber: Int,
    totalQuestions: Int,
    score: Int,
    timeLeftSeconds: Int,
    onAnswer: (String) -> Unit,
    isAnswerRevealed: Boolean,
    onNext: () -> Unit
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
                Text(
                    "Score: $score",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.semantics {
                        contentDescription = "Current score is $score points"
                        liveRegion = LiveRegionMode.Polite
                    }
                )
                Text(
                    text = "${timeLeftSeconds}s",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.semantics {
                        liveRegion = LiveRegionMode.Assertive
                    }
                )
                Text(
                    "Q$questionNumber/$totalQuestions",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.semantics {
                        contentDescription = "Question $questionNumber of $totalQuestions"
                    }
                )
            }
            Spacer(Modifier.height(24.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(AppSpacing.medium),
                modifier = Modifier.fillMaxWidth()
            ) {
                var selectedAnswer by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(selectedAnswer) {
                    if (selectedAnswer != null) {
                        onAnswer(selectedAnswer!!)
                    }
                }

                question.answers.forEach { answerText ->
                    AnswerOption(

                        answerText = answerText,
                        isSelected = selectedAnswer == answerText,
                        isCorrect = isAnswerRevealed && question.checkAnswer(answerText),
                        isActuallyCorrect = question.correctAnswer == answerText,
                        isRevealed = isAnswerRevealed,
                        onClick = { chosen ->
                            if (selectedAnswer == null) {
                                selectedAnswer = chosen
                            }
                        },
                        enabled = !isAnswerRevealed
                    )
                }

                if (isAnswerRevealed) {
                    Spacer(Modifier.height(AppSpacing.medium))
                    Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
                        Text("Next")
                    }
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
    isActuallyCorrect: Boolean, // New parameter
    isRevealed: Boolean,
    onClick: (String) -> Unit,
    enabled: Boolean = true
) {
    val successColor = Color(0xFF2E7D32)
    val wrongColor = Color(0xFFB00020)
    val defaultColor = MaterialTheme.colorScheme.primary

    val targetColor = when {
        !isRevealed -> defaultColor
        isSelected && !isCorrect -> wrongColor
        isActuallyCorrect -> successColor
        else -> defaultColor.copy(alpha = 0.6f)
    }

    val bgColor by animateColorAsState(targetColor, animationSpec = tween(durationMillis = 300))
    val scaleTarget = if (isSelected && isCorrect) 1.03f else 1f
    val scale by animateFloatAsState(targetValue = scaleTarget, animationSpec = tween(250))

    val stateDesc = when {
        !isRevealed -> "Not selected"
        isSelected && isCorrect -> "Correct answer"
        isSelected && !isCorrect -> "Incorrect answer"
        !isSelected && isActuallyCorrect -> "The correct answer"
        else -> "Not selected"
    }

    ElevatedButton(
        onClick = { onClick(answerText) },
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .semantics {
                contentDescription = "Answer option: $answerText"
                stateDescription = stateDesc
            },
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = bgColor,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = bgColor,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(answerText, style = MaterialTheme.typography.bodyLarge)
    }
}


// Previews
@Preview(showBackground = true)
@Composable
fun VerticalQuizPreview() {
    TheQuizzlerTheme {
        val sampleQuestion = GeneratedQuestion(
            questionText = "What is the capital of France?",
            answers = listOf("Paris", "London", "Berlin", "Madrid"),
            correctAnswer = "Paris",
            checkAnswer = {it == "Paris"},
            timeLimitSeconds = 15
        )
        VerticalQuizScreen(
            question = sampleQuestion,
            questionNumber = 1,
            totalQuestions = 10,
            score = 50,
            timeLeftSeconds = 10,
            onAnswer = {},
            isAnswerRevealed = false,
            onNext = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 480)
@Composable
fun HorizontalQuizPreview() {
    TheQuizzlerTheme {
        val sampleQuestion = GeneratedQuestion(
            questionText = "Which planet is known as the Red Planet?",
            answers = listOf("Earth", "Mars", "Jupiter", "Venus"),
            correctAnswer = "Mars",
            checkAnswer = {it == "Mars"},
            timeLimitSeconds = 15
        )
        HorizontalQuizScreen(
            question = sampleQuestion,
            questionNumber = 2,
            totalQuestions = 10,
            score = 70,
            timeLeftSeconds = 10,
            onAnswer = {},
            isAnswerRevealed = false,
            onNext = {}
        )
    }
}
