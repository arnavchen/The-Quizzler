package com.example.thequizzler.ui.screens.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thequizzler.dataPersistence.QuizRepository
import com.example.thequizzler.dataPersistence.models.Session
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

interface LeaderboardUiModel {
    val highScores: StateFlow<List<Session>>
}

/**
 * ViewModel that exposes the top high score sessions from the repository.
 */
class LeaderboardViewModel(repository: QuizRepository) : ViewModel(), LeaderboardUiModel {

    // Expose the repository's highScores Flow as a StateFlow for the UI to collect.
    override val highScores: StateFlow<List<Session>> = repository.highScores
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
