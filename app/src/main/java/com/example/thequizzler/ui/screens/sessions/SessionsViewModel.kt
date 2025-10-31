package com.example.thequizzler.ui.screens.sessions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thequizzler.dataPersistence.models.Session
import com.example.thequizzler.dataPersistence.repositories.SessionsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// Data class to hold the UI state for the SessionsScreen
data class SessionsUiState(
    val sessionList: List<Session> = listOf()
)

class SessionsViewModel(sessionsRepository: SessionsRepository) : ViewModel() {
    /**
     * Holds the UI state for the Sessions screen. The state is collected from the
     * repository's `allSessions` Flow and converted into a StateFlow.
     */
    val sessionsUiState: StateFlow<SessionsUiState> =
        // Use .map to transform the List<Session> into a SessionsUiState object
        sessionsRepository.allSessions.map { sessions ->
            SessionsUiState(sessionList = sessions)
        }.stateIn(
            scope = viewModelScope,
            // Keep the flow active for 5 seconds after the last collector disappears.
            // This prevents the flow from being restarted on configuration changes (like rotation).
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SessionsUiState() // Start with an empty list
        )
}
