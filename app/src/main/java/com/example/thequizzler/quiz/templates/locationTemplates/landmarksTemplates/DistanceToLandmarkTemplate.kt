package com.example.thequizzler.quiz.templates.locationTemplates.landmarksTemplates

import android.location.Location
import com.example.thequizzler.quiz.GeneratedQuestion
import com.example.thequizzler.quiz.QuestionServices
import com.example.thequizzler.quiz.QuizSettings
import com.example.thequizzler.quiz.templates.IQuestionTemplate
import com.example.thequizzler.quiz.templates.SimpleLocation
import com.example.thequizzler.quiz.templates.TemplateRequirement
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow
import kotlin.random.Random

object DistanceToLandmarkTemplate : IQuestionTemplate {

    // Requires location, but NOT internet.
    override val requirements = setOf(TemplateRequirement.LOCATION)

    override val weight = 1;

    private val landmarks = LandmarksData.landmarks

    override suspend fun generate(services: QuestionServices, location: SimpleLocation?, settings: QuizSettings): GeneratedQuestion? {
        if (location == null) {
            return null
        }

        val chosenLandmark = landmarks.random()

        // Calculate the distance to the randomly chosen landmark
        val distanceMeters = FloatArray(1)
        Location.distanceBetween(location.latitude, location.longitude, chosenLandmark.latitude, chosenLandmark.longitude, distanceMeters)

        // Don't ask if the user is basically on top of the landmark
        if (distanceMeters[0] < 500) {
            return null
        }

        val correctAnswer = formatDistance(distanceMeters[0].toInt(), settings.isImperialSystem)
        val distractors = generateDistractors(distanceMeters[0].toInt(), settings.isImperialSystem)
        val answers = (distractors + correctAnswer).shuffled()

        return GeneratedQuestion(
            questionText = "How far away is the ${chosenLandmark.name}?",
            answers = answers,
            correctAnswer = correctAnswer,
            checkAnswer = { userAnswer -> userAnswer == correctAnswer },
            timeLimitSeconds = 15
        )
    }

    private fun generateDistractors(correctDistance: Int, isImperial: Boolean): List<String> {
        val distractors = mutableSetOf<String>()
        val rnd = Random.Default
        val lowerLimit = 0.25
        val upperLimit = 4.0
        val deadZoneLowerBound = 0.8
        val deadZoneUpperBound = 1.2
        while (distractors.size < 3) {
            val randomFactor = when (rnd.nextBoolean()) {
                true -> rnd.nextDouble(lowerLimit, deadZoneLowerBound)
                false -> rnd.nextDouble(deadZoneUpperBound, upperLimit)
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
