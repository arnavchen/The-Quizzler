package com.example.thequizzler.ui.screens.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thequizzler.dataPersistence.repositories.HighScoresRepository
import com.example.thequizzler.dataPersistence.models.Session
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import com.example.thequizzler.ui.AppViewModelProvider

interface LeaderboardUiModel {
    val highScores: StateFlow<List<Session>>
}

/**
 * ViewModel that exposes the top high score sessions from the repository.
 */
class LeaderboardViewModel(repository: HighScoresRepository) : ViewModel(), LeaderboardUiModel {
    override val highScores: StateFlow<List<Session>> = repository.highScores
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
