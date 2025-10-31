package com.example.thequizzler.ui.screens.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thequizzler.dataPersistence.models.QuestionInstance
import com.example.thequizzler.dataPersistence.models.Session
import com.example.thequizzler.dataPersistence.repositories.HighScoresRepository
import com.example.thequizzler.dataPersistence.repositories.QuestionInstancesRepository
import com.example.thequizzler.dataPersistence.repositories.SessionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Define the structure for a generated question (will be updated later when more question types are designed)
data class GeneratedQuestion(
    val questionText: String,
    val answers: List<String>,
    val correctAnswer: String
)

// This will hold all the data for an active quiz
data class QuizState(
    val playerName: String = "",
    val score: Int = 0,
    val generatedQuestions: List<GeneratedQuestion> = emptyList(), // This will be dynamic later
    val answeredQuestions: List<QuestionInstance> = emptyList()
)

class QuizViewModel(
    private val sessionsRepository: SessionsRepository,
    private val questionInstancesRepository: QuestionInstancesRepository,
    private val highScoresRepository: HighScoresRepository
) : ViewModel() {

    private val _quizState = MutableStateFlow(QuizState())
    val quizState = _quizState.asStateFlow()

    fun startQuiz(playerName: String, questions: List<GeneratedQuestion>) {
        _quizState.value = QuizState(
            playerName = playerName,
            generatedQuestions = questions
        )
    }

    fun recordAnswer(
        questionNumber: Int,
        questionText: String,
        userAnswer: String,
        correctAnswer: String,
        wasCorrect: Boolean,
        pointsAwarded: Int,

    ) {
        val questionInstance = QuestionInstance(
            sessionId = -1, // We don't know the session ID yet, will be updated later
            question = questionText,
            userResponse = userAnswer,
            correctResponse = correctAnswer,
            questionNumber = questionNumber,
            points = pointsAwarded,
            wasCorrect = wasCorrect,
        )

        _quizState.value = _quizState.value.copy(
            score = _quizState.value.score + pointsAwarded,
            answeredQuestions = _quizState.value.answeredQuestions + questionInstance
        )
    }

    // The main function to save everything
    fun finalizeAndSaveQuiz() {
        // Run this in the background
        viewModelScope.launch {
            val finalState = _quizState.value
            val numCorrect = finalState.answeredQuestions.count { it.wasCorrect }

            // Create the Session object
            val sessionToSave = Session(
                userName = finalState.playerName,
                score = finalState.score,
                startTime = System.currentTimeMillis(),
                numCorrect = numCorrect,
            )

            // Insert the session and get its new ID
            val newSessionId = sessionsRepository.insertSession(sessionToSave)

            // Update the question instances with the correct session ID and save them
            val updatedQuestions = finalState.answeredQuestions.map {
                it.copy(sessionId = newSessionId.toInt())
            }
            questionInstancesRepository.insertAllQuestions(updatedQuestions)

            // Submit the score to the high scores repository
            // We pass the session with its new ID to the high score logic
            highScoresRepository.submitNewHighScore(sessionToSave.copy(id = newSessionId.toInt()))
        }
    }
}
