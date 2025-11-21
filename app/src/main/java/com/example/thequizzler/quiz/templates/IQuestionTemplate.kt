package com.example.thequizzler.quiz.templates

import com.example.thequizzler.quiz.GeneratedQuestion
import com.example.thequizzler.quiz.QuestionServices
import com.example.thequizzler.quiz.QuizSettings

// Defines the requirements a template may have to be able to be used
enum class TemplateRequirement {
    INTERNET,
    LOCATION,
    // Add more as we design more types of questions
}

/**
 * A simple, modern interface for all question templates.
 * Each template is a self-contained unit responsible for generating one question.
 */
interface IQuestionTemplate {

    /**
     * The set of requirements that must be met for this template to be used.
     * An empty set means the template can always run (e.g., for a simple offline question).
     */
    val requirements: Set<TemplateRequirement>

    /**
     * Generates a single question instance.
     * @param services A container for dependencies like the Places API repository.
     * @param settings The quiz settings (e.g., measurement system) to tailor generation.
     * @return A GeneratedQuestion, or null if generation fails.
     */
    suspend fun generate(services: QuestionServices, location: SimpleLocation?, settings: QuizSettings): GeneratedQuestion?
}
