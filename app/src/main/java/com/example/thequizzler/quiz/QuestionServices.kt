package com.example.thequizzler.quiz

import android.location.Location
import com.example.thequizzler.quiz.apiRepositories.PlacesRepository

/**
 * A provider for location services. This can be a real GPS implementation
 * or a mock one for testing.
 */
interface LocationProvider {
    fun getLastKnownLocation(): Location?
}

/**
 * A service locator that holds API repositories and the state for the
 * current question generation session.
 *
 * Instead of passing each item individually, we pass this single container.
 */
data class QuestionServices(
    // --- Repositories ---
    val placesRepository: PlacesRepository,
    // Add future repositories here
)
