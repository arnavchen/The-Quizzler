package com.example.thequizzler.quiz.templates

import com.example.thequizzler.quiz.templates.locationBased.DistanceToNearestTemplate
import com.example.thequizzler.quiz.templates.locationBased.WhichIsCloserTemplate

/**
 * A simple registry that holds a reference to all available question generator templates.
 */
object TemplatesRegistry {

    fun getAllTemplates(): List<IQuestionTemplate> {
        return listOf(
            WhichIsCloserTemplate,
            DistanceToNearestTemplate
            // Add other new template objects here as we create them
        )
    }
}
