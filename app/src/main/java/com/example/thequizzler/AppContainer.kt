package com.example.thequizzler

import android.content.Context
import com.example.thequizzler.dataPersistence.QuizDatabase
import com.example.thequizzler.dataPersistence.QuizRepository
import com.example.thequizzler.dataPersistence.SettingsRepository

/**
 * A container class for dependencies that are shared across the application.
 * This provides a simplified form of manual dependency injection.
 */
interface AppContainer {
    val quizzlerRepository: QuizRepository
    val settingsRepository: SettingsRepository
}

/**
 * The default implementation of AppContainer. It creates and holds instances
 * of the repositories, ensuring they are singletons.
 */
class DefaultAppContainer(private val context: Context) : AppContainer {

    // Lazily create the QuizzlerRepository. It won't be initialized until it's first accessed.
    // It gets its DAOs from the singleton QuizzlerDatabase instance.
    override val quizzlerRepository: QuizRepository by lazy {
        QuizRepository(
            QuizDatabase.getDatabase(context).sessionDao(),
            QuizDatabase.getDatabase(context).questionInstanceDao(),
            QuizDatabase.getDatabase(context).highScoreDao()
        )
    }

    // Lazily create the SettingsRepository.
    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(context)
    }
}
