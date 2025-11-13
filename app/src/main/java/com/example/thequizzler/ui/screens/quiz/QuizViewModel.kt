package com.example.thequizzler.ui.screens.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thequizzler.dataPersistence.models.QuestionInstance
import com.example.thequizzler.dataPersistence.models.Session
import com.example.thequizzler.dataPersistence.repositories.HighScoresRepository
import com.example.thequizzler.dataPersistence.repositories.QuestionInstancesRepository
import com.example.thequizzler.dataPersistence.repositories.SessionsRepository
import com.example.thequizzler.dataPersistence.repositories.SettingsRepository
import com.example.thequizzler.quiz.templates.PlacesRepository
import com.example.thequizzler.quiz.templates.TemplatesRegistry
import com.example.thequizzler.quiz.templates.QuestionGenerationContext
import com.example.thequizzler.quiz.templates.FakePlacesRepository
import com.example.thequizzler.quiz.templates.SimpleLocation
import kotlinx.coroutines.flow.first
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
    private val highScoresRepository: HighScoresRepository,
    private val settingsRepository: SettingsRepository,
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private val _quizState = MutableStateFlow(QuizState())
    val quizState = _quizState.asStateFlow()

    /**
     * Start a quiz. If `staticQuestions` is provided and non-empty it will be used directly.
     * Otherwise this will attempt to generate up to 10 questions using the templates.
     * Generation respects the settings repository: when offline mode is enabled, a local
     * fake repository will be used so no network calls are attempted.
     */
    fun startQuiz(
        playerName: String,
        staticQuestions: List<GeneratedQuestion>? = null,
        location: SimpleLocation? = null
    ) {
        // If the caller provides explicit static questions, use them immediately.
        if (!staticQuestions.isNullOrEmpty()) {
            _quizState.value = QuizState(playerName = playerName, generatedQuestions = staticQuestions)
            return
        }

        // Otherwise generate questions asynchronously based on settings and templates.
        viewModelScope.launch {
            val isOffline = settingsRepository.isOfflineMode.first()
            val isLocationEnabled = settingsRepository.isLocationEnabled.first()

            // If offline mode is on or location questions are disabled, use a fake repo to avoid network.
            val repoToUse: PlacesRepository = if (isOffline || !isLocationEnabled) FakePlacesRepository() else placesRepository

            val ctx = QuestionGenerationContext(location = location, placesRepo = repoToUse)
            val templates = TemplatesRegistry.allTemplates().shuffled()
            val instances = mutableListOf<com.example.thequizzler.quiz.templates.QuestionInstance>()
            for (t in templates) {
                try {
                    val inst = t.generate(ctx)
                    if (inst != null) instances += inst
                } catch (e: Exception) {
                    // Ignore generation errors for individual templates and continue
                }
                if (instances.size >= 10) break
            }

            val generated = instances.map {
                GeneratedQuestion(it.questionText, it.answers, it.correctAnswer)
            }

            _quizState.value = QuizState(playerName = playerName, generatedQuestions = generated)
        }
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
