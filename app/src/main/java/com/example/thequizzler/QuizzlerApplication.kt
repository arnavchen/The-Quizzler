package com.example.thequizzler

import android.app.Application

/**
 * A custom Application class to hold the application-wide dependency container.
 */
class QuizzlerApplication : Application() {
    /**
     * The AppContainer instance used by the rest of the app for dependency access.
     * It is initialized lazily on first access.
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        // Initialize the container when the application is created.
        container = DefaultAppContainer(this)
    }
}
