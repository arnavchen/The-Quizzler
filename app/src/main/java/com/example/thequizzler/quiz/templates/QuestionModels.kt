package com.example.thequizzler.quiz.templates

/**
 * Lightweight models used by question templates and generators.
 */
data class QuestionInstance(
    val id: String,
    val questionText: String,
    val answers: List<String>,
    val correctAnswer: String,
    val timeLimitSeconds: Int
)

/** Simple lat/lng holder to avoid depending on Android Location in the templates. */
data class SimpleLocation(val latitude: Double, val longitude: Double)

/** Minimal place result used by templates. distanceMeters is optional — generators may compute it. */
data class PlaceResult(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val distanceMeters: Int? = null
)

/**
 * Abstraction to get place data. Implementations may call Google Places or return a stub.
 * For the in-memory demo we add a FakePlacesRepository below (see TemplatesRegistry).
 */
interface PlacesRepository {
    suspend fun findNearestByKeyword(keyword: String, lat: Double, lng: Double): PlaceResult?
}

/**
 * Context passed into the templates so they can generate a concrete QuestionInstance.
 */
data class QuestionGenerationContext(
    val location: SimpleLocation?,
    val placesRepo: PlacesRepository
)
