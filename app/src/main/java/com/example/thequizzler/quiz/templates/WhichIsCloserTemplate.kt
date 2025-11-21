package com.example.thequizzler.quiz.templates

import com.example.thequizzler.quiz.GeneratedQuestion
import com.example.thequizzler.quiz.QuestionServices
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

object WhichIsCloserTemplate : IQuestionTemplate {

    override val supportedModes = setOf(QuizMode.LOCATION_ENABLED)

    // Keywords are now stored directly in the template
    private val candidateSets = listOf(
        listOf("Starbucks", "McDonald's", "Chipotle", "Chick-fil-A"),
        listOf("Pizza Hut", "Subway", "Panda Express", "Taco Bell"),
        listOf("Train station", "Airport", "Bus stop", "Ferry"),
        listOf("Mall", "Grocery store", "Bank", "Library"),
        listOf("Coffee shop", "Bakery", "Diner", "Tea house")
    )

    override suspend fun generate(services: QuestionServices): GeneratedQuestion? {
        val location = services.locationProvider.getLastKnownLocation() ?: return null
        val keywords = candidateSets.random()

        // Fetch place results concurrently for speed
        val results = coroutineScope {
            keywords.map { keyword ->
                async {
                    services.placesRepository.findNearestByKeyword(keyword, location.latitude, location.longitude)
                }
            }.awaitAll()
        }.filterNotNull()

        if (results.size < 2) return null // Need at least two places to compare

        val sorted = results.sortedBy { it.distanceMeters }
        val correctAnswer = sorted.first().name
        val answerChoices = results.map { it.name }.distinct().shuffled().take(4)

        if (answerChoices.size < 2) return null

        return GeneratedQuestion(
            questionText = "Which of these is closest to you right now?",
            answers = answerChoices,
            correctAnswer = correctAnswer,
            checkAnswer = { userAnswer -> userAnswer == correctAnswer },
            timeLimitSeconds = 20
        )
    }
}
