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
            return null;
        }

        val weightedTemplates = availableTemplates.flatMap { template ->
            List(template.weight) { template }
        }

        return coroutineScope {
            // Launch [count] concurrent jobs, one for each question needed.
            val questionJobs = (1..count).map {
                async {
                    // Each thread tries to generate a question 5 times before returning null
                    var generatedQuestion: GeneratedQuestion? = null
                    for (attempt in 1..5) {
                        val candidate = weightedTemplates.random().generate(services, location, settings)
                        if (candidate != null) {
                            generatedQuestion = candidate
                            break // Successfully generated, break the inner loop.
                        }
                    }
                    generatedQuestion // Return the result (or null if all attempts failed).
                }
            }

            // Wait for all threads to complete.
            val results = questionJobs.awaitAll()

            // Filter out any nulls and ensure distinctness.
            val successfulQuestions = results.filterNotNull().distinct()

            // Check if [count] questions were successfully generated. If not, return null
            if (successfulQuestions.size < count) {
                null
            } else {
                successfulQuestions
            }
        }
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
