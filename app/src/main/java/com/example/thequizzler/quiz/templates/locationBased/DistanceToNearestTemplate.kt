package com.example.thequizzler.quiz.templates.locationBased

import android.util.Log
import com.example.thequizzler.quiz.GeneratedQuestion
import com.example.thequizzler.quiz.QuestionServices
import com.example.thequizzler.quiz.apiRepositories.PlaceResult
import com.example.thequizzler.quiz.templates.IQuestionTemplate
import com.example.thequizzler.quiz.templates.SimpleLocation
import com.example.thequizzler.quiz.templates.TemplateRequirement
import kotlin.random.Random

object DistanceToNearestTemplate : IQuestionTemplate {

    override val requirements = setOf(TemplateRequirement.LOCATION, TemplateRequirement.INTERNET)

    // Keywords are stored here
    private val franchises = PlacesKeywords.franchises
    private val placeTypes = PlacesKeywords.placeTypes


    override suspend fun generate(services: QuestionServices, location: SimpleLocation?): GeneratedQuestion? {

        if(location == null) {
            return null;
        }

        val nearest: PlaceResult?
        val chosenKeyword: String

        if (Random.Default.nextBoolean()) {
            chosenKeyword = franchises.random()
            Log.d("TemplateDebug", "Searching by NAME: $chosenKeyword")
            nearest = services.placesRepository.findNearestPlaceByFranchiseName(chosenKeyword, location.latitude, location.longitude)
        } else {
            chosenKeyword = placeTypes.random()
            Log.d("TemplateDebug", "Searching by TYPE: $chosenKeyword")
            nearest = services.placesRepository.findNearestPlaceByType(chosenKeyword, location.latitude, location.longitude)
        }

        Log.d("Place chosen", chosenKeyword);

        if(nearest?.distanceMeters == null || nearest.distanceMeters < 15) {
            return null;
        }

        val correctAnswer = formatDistance(nearest.distanceMeters)
        val distractors = generateDistractors(nearest.distanceMeters)
        val answers = (distractors + correctAnswer).shuffled()

        return GeneratedQuestion(
            questionText = "How far is the nearest $chosenKeyword from you?",
            answers = answers,
            correctAnswer = correctAnswer,
            checkAnswer = { userAnswer -> userAnswer == correctAnswer },
            timeLimitSeconds = 15
        )

    }

    private fun generateDistractors(correctDistance: Int): List<String> {
        val distractors = mutableSetOf<String>()
        val rnd = Random.Default

        // Generate random answers from 25% to 400% of correct answer, but not between 80% and 120%
        val lowerLimit = 0.25
        val upperLimit = 4.0
        val deadZoneLowerBound = 0.8
        val deadZoneUpperBound = 1.2

        while (distractors.size < 3) {
            val randomFactor = when (rnd.nextBoolean()) {
                true -> rnd.nextDouble(lowerLimit, deadZoneLowerBound) // Values smaller than the correct answer
                false -> rnd.nextDouble(deadZoneUpperBound, upperLimit) // Values larger than the correct answer
            }

            val distractorValue = (correctDistance * randomFactor).toInt().coerceAtLeast(10)

            distractors.add(formatDistance(distractorValue))
        }
        return distractors.toList()
    }

    private fun formatDistance(meters: Int): String {
        return if (meters >= 1000) {
            String.format("%.1f km", meters / 1000.0)
        } else {
            "$meters m"
        }
    }
}