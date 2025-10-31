package com.example.thequizzler.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thequizzler.dataPersistence.repositories.SessionsRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

// REMOVED the empty data class
// data class HomeUiState()

class HomeViewModel(private val sessionsRepository: SessionsRepository) : ViewModel() {

    // Holds the name entered in the name box
    var playerName by mutableStateOf("")
        private set

    // A flag to ensure we only fetch from the database once.
    private var isInitialLoad = true

    // Fetch the player name upon initialization of viewModel
    init {
        fetchMostRecentPlayerName()
    }

    // This function will be called by the UI to update the name
    fun onNameChange(newName: String) {
        playerName = newName
    }

    private fun fetchMostRecentPlayerName() {
        // Only run this logic if it's the first time the ViewModel is loaded.
        if (isInitialLoad) {
            viewModelScope.launch {

                val mostRecentSession = sessionsRepository.allSessions.firstOrNull()?.firstOrNull()

                if (mostRecentSession != null) {
                    playerName = mostRecentSession.userName
                }
                // Mark that the initial load is complete.
                isInitialLoad = false
            }
        }
    }
}
