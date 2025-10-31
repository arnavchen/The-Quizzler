package com.example.thequizzler.ui.screens.sessioninfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thequizzler.dataPersistence.models.QuestionInstance
import com.example.thequizzler.dataPersistence.models.Session
import com.example.thequizzler.dataPersistence.repositories.QuestionInstancesRepository
import com.example.thequizzler.dataPersistence.repositories.SessionsRepository
import kotlinx.coroutines.flow.*

/**
 * UI state for the SessionInfo screen
 */
data class SessionInfoUiState(
    val session: Session? = null,
    val questions: List<QuestionInstance> = listOf()
)

class SessionInfoViewModel(
    savedStateHandle: SavedStateHandle,
    sessionsRepository: SessionsRepository,
    questionInstancesRepository: QuestionInstancesRepository
) : ViewModel() {

    // Get the sessionId from the navigation arguments
    private val sessionId: Int = checkNotNull(savedStateHandle.get<String>("sessionId")).toInt()

    val uiState: StateFlow<SessionInfoUiState> =
        // Combine two flows: one for the session and one for the questions
        combine(
            sessionsRepository.getSessionById(sessionId),
            questionInstancesRepository.getQuestionsForSession(sessionId)
        ) { session, questions ->
            SessionInfoUiState(session, questions)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SessionInfoUiState() // Initial state is empty
        )
}