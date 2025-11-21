package com.example.thequizzler.quiz.templates

import com.example.thequizzler.quiz.GeneratedQuestion
import com.example.thequizzler.quiz.QuestionServices
import kotlin.random.Random

object DistanceToNearestTemplate : IQuestionTemplate {

    override val supportedModes = setOf(QuizMode.LOCATION_ENABLED, QuizMode.OFFLINE)

    // Keywords are stored here
    private val keywords = listOf(
        "Starbucks", "park", "pharmacy", "hospital", "gas station", "museum"
    )

    override suspend fun generate(services: QuestionServices, location: SimpleLocation?): GeneratedQuestion? {
        val keyword = keywords.random()

        if(location == null) {
            return null;
        }

        val nearest = services.placesRepository.findNearestByKeyword(keyword, location.latitude, location.longitude)

        if(nearest?.distanceMeters == null) {
            return null;
        }

        val correctAnswer = formatDistance(nearest.distanceMeters)
        val distractors = generateDistractors(nearest.distanceMeters)
        val answers = (distractors + correctAnswer).shuffled()

        return GeneratedQuestion(
            questionText = "How far is the nearest $keyword from you?",
            answers = answers,
            correctAnswer = correctAnswer,
            checkAnswer = { userAnswer -> userAnswer == correctAnswer },
            timeLimitSeconds = 15
        )

    }

    private fun generateDistractors(correctDistance: Int): List<String> {
        val distractors = mutableSetOf<String>()
        val rnd = Random(correctDistance)
        while (distractors.size < 3) {
            val offset = when (rnd.nextInt(3)) {
                0 -> (correctDistance * (1 + rnd.nextDouble(0.2, 0.5))).toInt()
                1 -> (correctDistance * (1 - rnd.nextDouble(0.2, 0.5))).toInt()
                else -> (correctDistance * rnd.nextDouble(1.5, 4.0)).toInt()
            }.coerceAtLeast(10)

            if (formatDistance(offset) != formatDistance(correctDistance)) {
                distractors.add(formatDistance(offset))
            }
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
