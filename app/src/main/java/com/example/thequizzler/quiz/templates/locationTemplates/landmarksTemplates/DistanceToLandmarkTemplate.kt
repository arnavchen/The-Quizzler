package com.example.thequizzler.quiz.templates.locationTemplates.landmarksTemplates

import android.location.Location
import com.example.thequizzler.quiz.GeneratedQuestion
import com.example.thequizzler.quiz.QuestionServices
import com.example.thequizzler.quiz.QuizSettings
import com.example.thequizzler.quiz.templates.IQuestionTemplate
import com.example.thequizzler.quiz.templates.SimpleLocation
import com.example.thequizzler.quiz.templates.TemplateRequirement
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
        if (isImperial) {
            val metersPerMile = 1609.344
            val miles = meters / metersPerMile
            // For large distances, don't show feet.
            return String.format("%.0f mi", miles)
        } else {
            val kilometers = meters / 1000.0
            return String.format("%.0f km", kilometers)
        }
    }
}
