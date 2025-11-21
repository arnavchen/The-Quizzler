package com.example.thequizzler.quiz.templates

import com.example.thequizzler.quiz.GeneratedQuestion
import com.example.thequizzler.quiz.QuestionServices

// Defines the modes a template can operate in.
enum class QuizMode {
    OFFLINE,
    ONLINE_NO_LOCATION,
    LOCATION_ENABLED
}

/**
 * A simple, modern interface for all question templates.
 * Each template is a self-contained unit responsible for generating one question.
 */
interface IQuestionTemplate {
    /**
     * The quiz modes that this template supports. A template can support multiple modes.
     * For example, a template might have a real implementation for LOCATION_ENABLED
     * but a mock fallback for OFFLINE.
     */
    val supportedModes: Set<QuizMode>

    /**
     * Generates a single question instance.
     * @param services A container for dependencies like the Places API repository.
     * @return A GeneratedQuestion, or null if generation fails.
     */
    suspend fun generate(services: QuestionServices, location: SimpleLocation?): GeneratedQuestion?
}
