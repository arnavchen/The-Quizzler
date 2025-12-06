package com.example.thequizzler.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.thequizzler.QuizzlerApplication
import com.example.thequizzler.ui.screens.home.HomeViewModel
import com.example.thequizzler.ui.screens.leaderboard.LeaderboardViewModel
import com.example.thequizzler.ui.screens.quiz.QuizViewModel
import com.example.thequizzler.ui.screens.sessioninfo.SessionInfoViewModel
import com.example.thequizzler.ui.screens.sessions.SessionsViewModel
import com.example.thequizzler.ui.screens.settings.SettingsViewModel

/**
 * Provides a Factory to create instances of ViewModels.
 */
object AppViewModelProvider {
    var Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(
                // Get the SessionsRepository from the application's dependency container
                quizzlerApplication().container.sessionRepository
            )
        }

        // Initializer for LeaderboardViewModel
        initializer {
            LeaderboardViewModel(
                quizzlerApplication().container.highScoresRepository
            )
        }

        // Initializer for SettingsViewModel
        initializer {
            SettingsViewModel(
                quizzlerApplication().container.settingsRepository
            )
        }

        // Initializer for SessionsViewModel
        initializer {
            SessionsViewModel(
                quizzlerApplication().container.sessionRepository
            )
        }

        // Initializer for SessionInfoViewModel
        initializer {
            SessionInfoViewModel(
                // The SavedStateHandle is automatically provided
                this.createSavedStateHandle(),
                quizzlerApplication().container.sessionRepository,
                quizzlerApplication().container.questionInstanceRepository
            )
        }

        // Add the initializer for QuizViewModel
        initializer {
            QuizViewModel(
                quizzlerApplication().container.sessionRepository,
                quizzlerApplication().container.questionInstanceRepository,
                quizzlerApplication().container.highScoresRepository,
                quizzlerApplication().container.settingsRepository,
                quizzlerApplication().container.questionServices,
            )
        }
    }
}

/**
 * Extension function to get the QuizzlerApplication instance from CreationExtras.
 * This is a helper to make the ViewModel initializers cleaner.
 */
fun CreationExtras.quizzlerApplication(): QuizzlerApplication {
    return (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as QuizzlerApplication)
}
