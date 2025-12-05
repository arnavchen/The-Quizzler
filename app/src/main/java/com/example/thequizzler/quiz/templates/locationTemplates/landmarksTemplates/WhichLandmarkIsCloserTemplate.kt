package com.example.thequizzler.quiz.templates.locationTemplates.landmarksTemplates

import android.location.Location
import com.example.thequizzler.quiz.GeneratedQuestion
import com.example.thequizzler.quiz.QuestionServices
import com.example.thequizzler.quiz.QuizSettings
import com.example.thequizzler.quiz.templates.IQuestionTemplate
import com.example.thequizzler.quiz.templates.SimpleLocation
import com.example.thequizzler.quiz.templates.TemplateRequirement

object WhichLandmarkIsCloserTemplate : IQuestionTemplate {

    // Requires location, but NOT internet.
    override val requirements = setOf(TemplateRequirement.LOCATION)

    private val landmarks = LandmarksData.landmarks

    override suspend fun generate(services: QuestionServices, location: SimpleLocation?, settings: QuizSettings): GeneratedQuestion? {
        if (location == null || landmarks.size < 4) return null

        // Pick 4 random landmarks from the list
        val chosenLandmarks = landmarks.shuffled().take(4)

        // Calculate the distance to each one and find the closest
        val closestLandmark = chosenLandmarks.minByOrNull { landmark ->
            val distance = FloatArray(1)
            Location.distanceBetween(location.latitude, location.longitude, landmark.latitude, landmark.longitude, distance)
            distance[0]
        } ?: return null // Should not happen

        val correctAnswer = closestLandmark.name
        val answerChoices = chosenLandmarks.map { it.name }.shuffled()

        return GeneratedQuestion(
            questionText = "Which of these landmarks is closest to you?",
            answers = answerChoices,
            correctAnswer = correctAnswer,
            checkAnswer = { userAnswer -> userAnswer == correctAnswer },
            timeLimitSeconds = 15
        )
    }
}
