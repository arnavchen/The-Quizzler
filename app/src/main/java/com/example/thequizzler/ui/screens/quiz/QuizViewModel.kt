package com.example.thequizzler.ui.screens.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thequizzler.dataPersistence.models.Session
import com.example.thequizzler.dataPersistence.repositories.HighScoresRepository
import com.example.thequizzler.dataPersistence.repositories.QuestionInstancesRepository
import com.example.thequizzler.dataPersistence.repositories.SessionsRepository
import com.example.thequizzler.dataPersistence.repositories.SettingsRepository
import com.example.thequizzler.quiz.QuestionGenerator
import com.example.thequizzler.quiz.QuestionServices
import com.example.thequizzler.quiz.QuizManager
import com.example.thequizzler.quiz.QuizSettings
import com.example.thequizzler.quiz.apiRepositories.PlacesRepository
import com.example.thequizzler.quiz.templates.SimpleLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// This UI state is now much simpler, only holding the QuizManager and loading status.
data class QuizUiState(
    val quizManager: QuizManager? = null,
    val isLoading: Boolean = true,
    val playerName: String = ""
)

class QuizViewModel(
    private val sessionsRepository: SessionsRepository,
    private val questionInstancesRepository: QuestionInstancesRepository,
    private val highScoresRepository: HighScoresRepository,
    private val settingsRepository: SettingsRepository,
    private val questionServices: QuestionServices,
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Prepares and starts a new quiz.
     * It generates questions and initializes the QuizManager.
     */
    fun startQuiz(playerName: String) {
        viewModelScope.launch {
            _uiState.value = QuizUiState(isLoading = true, playerName = playerName)

            // Get settings
            val settings = QuizSettings(
                isOfflineMode = settingsRepository.isOfflineMode.first(),
                isLocationEnabled = settingsRepository.isLocationEnabled.first()
            )

            // Generate questions
            val generator = QuestionGenerator(settings, questionServices)
            val questions = generator.generateQuestions(10, location)

            // Create the manager and update the UI state
            val manager = QuizManager(questions)
            _uiState.value = QuizUiState(quizManager = manager, isLoading = false, playerName = playerName)
        }
    }

    /**
     * Finalizes and saves the quiz session to the database.
     */
    fun finalizeAndSaveQuiz() {
        val manager = _uiState.value.quizManager ?: return
        val playerName = _uiState.value.playerName

        viewModelScope.launch {
            val answeredQuestions = manager.getResults()
            val numCorrect = answeredQuestions.count { it.wasCorrect }

            val sessionToSave = Session(
                userName = playerName,
                score = manager.score,
                startTime = System.currentTimeMillis(),
                numCorrect = numCorrect
            )

            val newSessionId = sessionsRepository.insertSession(sessionToSave)

            val updatedQuestions = answeredQuestions.map {
                it.copy(sessionId = newSessionId.toInt())
            }
            questionInstancesRepository.insertAllQuestions(updatedQuestions)

            highScoresRepository.submitNewHighScore(sessionToSave.copy(id = newSessionId.toInt()))
        }
    }
}
