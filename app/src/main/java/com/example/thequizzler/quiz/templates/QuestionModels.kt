package com.example.thequizzler.quiz.templates

import com.example.thequizzler.quiz.QuestionServices

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

/**
 * Context passed into the templates so they can generate a concrete QuestionInstance.
 */
data class QuestionGenerationContext(
    val location: SimpleLocation?,
    val services: QuestionServices
)