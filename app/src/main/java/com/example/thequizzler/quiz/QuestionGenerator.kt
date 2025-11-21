package com.example.thequizzler.quiz

import com.example.thequizzler.quiz.templates.SimpleLocation
import com.example.thequizzler.quiz.templates.TemplateRequirement
import com.example.thequizzler.quiz.templates.TemplatesRegistry
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class QuestionGenerator(
    private val settings: QuizSettings,
    private val services: QuestionServices,
    private val location: SimpleLocation?
) {
    suspend fun generateQuestions(count: Int): List<GeneratedQuestion>? {

        // Build a set of currently satisfied requirements from the quiz settings.
        val satisfiedRequirements = mutableSetOf<TemplateRequirement>()
        if (settings.isLocationEnabled) satisfiedRequirements.add(TemplateRequirement.LOCATION)
        if (!settings.isOfflineMode) satisfiedRequirements.add(TemplateRequirement.INTERNET)

        // Filter available templates. A template is usable if its 'requirements' set
        // is a subset of the 'satisfiedRequirements' set.
        val availableTemplates = TemplatesRegistry.getAllTemplates().filter { template ->
            satisfiedRequirements.containsAll(template.requirements)
        }

        if (availableTemplates.isEmpty()) {
            return emptyList() // No templates available for this mode
        }

        val questions = mutableSetOf<GeneratedQuestion>()
        val maxAttempts = count * 5 // Safety break to prevent infinite loops

        for (i in 0 until maxAttempts) {
            // If we have enough questions, stop trying to generate more.
            if (questions.size >= count) break

            // Generate one question at a time.
            val newQuestion = availableTemplates.random().generate(services, location, settings)
            if (newQuestion != null) {
                // Add the question to a set to automatically handle distinctness.
                questions.add(newQuestion)
            }
        }

        // If, after all attempts, we still don't have enough questions, consider it a failure.
        if (questions.size < count) {
            return null
        }

        // Return a distinct list to avoid duplicate questions if the pool is small
        return questions.take(count)
    }
}

/**
 * A simple data class to hold settings relevant to question generation.
 */
data class QuizSettings(
    val isOfflineMode: Boolean,
    val isLocationEnabled: Boolean,
    val isImperialSystem: Boolean,
)
