package com.example.thequizzler.quiz

import com.example.thequizzler.dataPersistence.models.QuestionInstance
import kotlin.math.roundToInt

/**
 * Manages the state and logic of a single quiz session.
 * This class is not tied to the Android Framework and can be tested easily.
 *
 * @param generatedQuestions The list of questions for this quiz.
 */
class QuizManager(private val generatedQuestions: List<GeneratedQuestion>) {

    var score: Int = 0
        private set

    var currentQuestionIndex: Int = 0
        private set

    private val answeredQuestions = mutableListOf<QuestionInstance>()

    val totalQuestions: Int
        get() = generatedQuestions.size

    val isFinished: Boolean
        get() = currentQuestionIndex >= totalQuestions

    val currentQuestion: GeneratedQuestion?
        get() = generatedQuestions.getOrNull(currentQuestionIndex)

    /**
     * Processes a user's answer, calculates the score, and advances to the next question.
     *
     * @param userAnswer The answer selected by the user.
     * @param timeTakenMs The time in milliseconds it took the user to answer.
     * @return True if the answer was correct, false otherwise.
     */
    fun submitAnswer(userAnswer: String, timeTakenMs: Long): Boolean {
        val question = currentQuestion ?: return false

        val wasCorrect = question.checkAnswer(userAnswer)
        val pointsAwarded = if (wasCorrect) {
            val timePercentage = 1.0 - (timeTakenMs.toDouble() / (question.timeLimitSeconds * 1000.0))
            (100 * timePercentage.coerceIn(0.0, 1.0)).roundToInt()
        } else {
            0
        }

        score += pointsAwarded

        // Record the answered question for saving later
        answeredQuestions.add(
            QuestionInstance(
                sessionId = -1, // This will be updated by QuizViewModel before saving
                questionNumber = currentQuestionIndex + 1,
                points = pointsAwarded,
                question = question.questionText,
                userResponse = userAnswer,
                wasCorrect = wasCorrect,
                correctResponse = question.correctAnswer
            )
        )

        return wasCorrect
    }

    fun advanceToNextQuestion() {
        if (!isFinished) {
            currentQuestionIndex++
        }
    }

    /**
     * Returns the results of the quiz, ready to be saved to the database.
     */
    fun getResults(): List<QuestionInstance> {
        return answeredQuestions.toList()
    }
}