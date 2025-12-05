package com.example.thequizzler.quiz.templates

import com.example.thequizzler.quiz.templates.locationTemplates.landmarksTemplates.DistanceToLandmarkTemplate
import com.example.thequizzler.quiz.templates.locationTemplates.landmarksTemplates.WhichLandmarkIsCloserTemplate
import com.example.thequizzler.quiz.templates.locationTemplates.placesTemplates.DistanceToNearestPlaceTemplate
import com.example.thequizzler.quiz.templates.locationTemplates.placesTemplates.WhichPlaceIsCloserTemplate
import com.example.thequizzler.quiz.templates.offlineTemplates.WhichCameFirstTemplate

/**
 * A simple registry that holds a reference to all available question generator templates.
 */
object TemplatesRegistry {

    fun getAllTemplates(): List<IQuestionTemplate> {
        return listOf(
            WhichPlaceIsCloserTemplate,
            DistanceToNearestPlaceTemplate,
            WhichCameFirstTemplate,
            DistanceToLandmarkTemplate,
            WhichLandmarkIsCloserTemplate,
            // Add other new template objects here as we create them
        )
    }
}
