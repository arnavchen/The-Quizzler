package com.example.thequizzler.quiz.templates.locationTemplates.placesTemplates

import android.util.Log
import com.example.thequizzler.quiz.GeneratedQuestion
import com.example.thequizzler.quiz.QuestionServices
import com.example.thequizzler.quiz.QuizSettings
import com.example.thequizzler.quiz.apiRepositories.PlaceResult
import com.example.thequizzler.quiz.templates.IQuestionTemplate
import com.example.thequizzler.quiz.templates.SimpleLocation
import com.example.thequizzler.quiz.templates.TemplateRequirement
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow
import kotlin.random.Random

object DistanceToNearestPlaceTemplate : IQuestionTemplate {

    override val requirements = setOf(TemplateRequirement.LOCATION, TemplateRequirement.INTERNET)

    override val weight = 3;

    // Keywords are stored here
    private val franchises = PlacesKeywords.franchises
    private val placeTypes = PlacesKeywords.placeTypes


    override suspend fun generate(services: QuestionServices, location: SimpleLocation?, settings: QuizSettings): GeneratedQuestion? {

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

            val (prettyName, apiType) = placeTypes.entries.random()
            chosenKeyword = prettyName
            Log.d("TemplateDebug", "Searching by TYPE: $apiType")
            nearest = services.placesRepository.findNearestPlaceByType(apiType, location.latitude, location.longitude)
        }

        Log.d("Place chosen", chosenKeyword);

        if(nearest?.distanceMeters == null || nearest.distanceMeters < 15) {
            return null;
        }

        val correctAnswer = formatDistance(nearest.distanceMeters, settings.isImperialSystem)
        val distractors = generateDistractors(nearest.distanceMeters, settings.isImperialSystem)
        val answers = (distractors + correctAnswer).shuffled()

        return GeneratedQuestion(
            questionText = "How far is the nearest $chosenKeyword from you?",
            answers = answers,
            correctAnswer = correctAnswer,
            checkAnswer = { userAnswer -> userAnswer == correctAnswer },
            timeLimitSeconds = 15
        )

    }

    private fun generateDistractors(correctDistance: Int, isImperial: Boolean): List<String> {
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

            val distractorValue = (correctDistance * randomFactor).toInt()

            distractors.add(formatDistance(distractorValue, isImperial))
        }
        return distractors.toList()
    }

    private fun formatDistance(meters: Int, isImperial: Boolean): String {
        return if (isImperial) {
            val miles = meters / 1609.344
            if (miles < 0.2) { // Use feet for short distances (approx. < 1000 ft)
                val feet = meters * 3.28084
                "${formatToTwoSignificantFigures(feet)} ft"
            } else { // Use miles for longer distances
                "${formatToTwoSignificantFigures(miles)} mi"
            }
        } else { // Metric
            if (meters < 1000) {
                // Use meters for short distances
                "${formatToTwoSignificantFigures(meters.toDouble())} m"
            } else { // Use kilometers for longer distances
                val kilometers = meters / 1000.0
                "${formatToTwoSignificantFigures(kilometers)} km"
            }
        }
    }

    private fun formatToTwoSignificantFigures(value: Double): String {
        if (value == 0.0) return "0"

        if (value < 1.0) {
            val magnitude = floor(log10(value)).toInt()
            val decimals = 1 - magnitude

            return String.format("%,.${decimals}f", value)
        }

        val magnitude = floor(log10(value))
        val scale = 10.0.pow(magnitude - 1)

        val roundedValue = kotlin.math.round(value / scale) * scale

        val showAsInteger = roundedValue >= 10 || roundedValue % 1.0 == 0.0
        return if (showAsInteger) {
            String.format("%,.0f", roundedValue)
        } else {
            String.format("%,.1f", roundedValue)
        }
    }

}