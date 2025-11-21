package com.example.thequizzler.quiz.templates.locationBased

import android.util.Log
import com.example.thequizzler.quiz.GeneratedQuestion
import com.example.thequizzler.quiz.QuestionServices
import com.example.thequizzler.quiz.apiRepositories.PlaceResult
import com.example.thequizzler.quiz.templates.IQuestionTemplate
import com.example.thequizzler.quiz.QuizSettings
import com.example.thequizzler.quiz.templates.SimpleLocation
import com.example.thequizzler.quiz.templates.TemplateRequirement
import com.google.android.libraries.places.api.Places
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.random.Random

object WhichIsCloserTemplate : IQuestionTemplate {

    override val requirements = setOf(TemplateRequirement.LOCATION, TemplateRequirement.INTERNET)

    private val franchises = PlacesKeywords.franchises
    private val placeTypes = PlacesKeywords.placeTypes

    override suspend fun generate(services: QuestionServices, location: SimpleLocation?, settings: QuizSettings): GeneratedQuestion? {

        if(location == null) {
            return null;
        }


        val useFranchises = Random.Default.nextBoolean()
        val sourceList = if (useFranchises) franchises else placeTypes

        val keywords = sourceList.shuffled().take(4)

        Log.d("Keywords Chosen", keywords.toString());

        // Fetch place results concurrently for speed
        val results: List<PlaceResult> = coroutineScope {
            keywords.map { keyword ->
                async {
                    if (useFranchises) {
                        services.placesRepository.findNearestPlaceByFranchiseName(
                            keyword,
                            location.latitude,
                            location.longitude
                        )
                    } else {
                        services.placesRepository.findNearestPlaceByType(
                            keyword,
                            location.latitude,
                            location.longitude
                        )
                    }
                }
            }.awaitAll()
        }.filterNotNull()

        if (results.size < 4) {
            return null
        }

        val sorted = results.sortedBy { it.distanceMeters }
        val correctAnswer = sorted.first().name
        val answerChoices = results.map { it.name }.distinct().shuffled().take(4)

        if (answerChoices.size < 4) {
            return null
        } // Double checking to be extra safe

        return GeneratedQuestion(
            questionText = "Which of these is closest to you right now?",
            answers = answerChoices,
            correctAnswer = correctAnswer,
            checkAnswer = { userAnswer -> userAnswer == correctAnswer },
            timeLimitSeconds = 15
        )
    }
}