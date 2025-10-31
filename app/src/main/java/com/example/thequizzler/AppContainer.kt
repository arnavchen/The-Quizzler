package com.example.thequizzler

import android.content.Context
import com.example.thequizzler.dataPersistence.QuizDatabase
import com.example.thequizzler.dataPersistence.repositories.HighScoresRepository
import com.example.thequizzler.dataPersistence.repositories.QuestionInstancesRepository
import com.example.thequizzler.dataPersistence.repositories.SessionsRepository
import com.example.thequizzler.dataPersistence.repositories.SettingsRepository

/**
 * A container class for dependencies that are shared across the application.
 * This provides a simplified form of manual dependency injection.
 */
interface AppContainer {

    val sessionRepository: SessionsRepository
    val questionInstanceRepository: QuestionInstancesRepository
    val highScoresRepository: HighScoresRepository
    val settingsRepository: SettingsRepository
}

/**
 * The default implementation of AppContainer. It creates and holds instances
 * of the repositories, ensuring they are singletons.
 */
class DefaultAppContainer(private val context: Context) : AppContainer {

    // Lazily create the SessionRepository.
    override val sessionRepository: SessionsRepository by lazy {
        SessionsRepository(QuizDatabase.getDatabase(context).sessionDao())
    }

    // Lazily create the QuestionInstanceRepository.
    override val questionInstanceRepository: QuestionInstancesRepository by lazy {
        QuestionInstancesRepository(QuizDatabase.getDatabase(context).questionInstanceDao())
    }

    // Lazily create the HighScoresRepository.
    override val highScoresRepository: HighScoresRepository by lazy {
        HighScoresRepository(QuizDatabase.getDatabase(context).highScoreDao())
    }

    // Lazily create the SettingsRepository.
    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(context)
    }
}
