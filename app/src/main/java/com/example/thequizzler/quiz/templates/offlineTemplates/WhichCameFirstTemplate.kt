package com.example.thequizzler.quiz.templates.offlineTemplates

import com.example.thequizzler.quiz.GeneratedQuestion
import com.example.thequizzler.quiz.QuestionServices
import com.example.thequizzler.quiz.QuizSettings
import com.example.thequizzler.quiz.templates.IQuestionTemplate
import com.example.thequizzler.quiz.templates.SimpleLocation
import com.example.thequizzler.quiz.templates.TemplateRequirement
import kotlin.math.abs

/**
 * An offline template that asks the user which of two historical events occurred first.
 */
object WhichCameFirstTemplate : IQuestionTemplate {

    // This template has no requirements, so it can run in any mode.
    override val requirements: Set<TemplateRequirement> = emptySet()

    override val weight = 1;

    private val events = OfflineData.historicalEvents

    override suspend fun generate(
        services: QuestionServices,
        location: SimpleLocation?,
        settings: QuizSettings
    ): GeneratedQuestion? {
        // Ensure we have at least two events to choose from.
        if (events.size < 2) return null

        // Pick two different random events.
        val (event1Name, event1Year) = events.random()
        var (event2Name, event2Year) = events.random()

        // Make sure the events are not the same and not from the same year,
        // and not too far apart to make it too easy.
        var attempts = 0
        while (event1Name == event2Name || event1Year == event2Year || abs(event1Year - event2Year) > 500) {
            if (attempts++ > 20) return null // Safety break
            val newEvent = events.random()
            event2Name = newEvent.first
            event2Year = newEvent.second
        }

        val correctAnswer = if (event1Year < event2Year) event1Name else event2Name
        val answers = listOf(event1Name, event2Name).shuffled()

        return GeneratedQuestion(
            questionText = "Which of these events came first?",
            answers = answers,
            correctAnswer = correctAnswer,
            checkAnswer = { userAnswer -> userAnswer == correctAnswer },
            timeLimitSeconds = 15
        )
    }
}
