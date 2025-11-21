package com.example.thequizzler.quiz

/**
 * A standardized data class representing a single generated question.
 * This class is used by the QuestionGenerator, QuizManager, and the UI.
 *
 * @param questionText The text of the question to be displayed.
 * @param answers A list of possible answers.
 * @param checkAnswer A function that takes a user's answer string and returns true if it's correct.
 * @param correctAnswer A string representing the canonical correct answer, used for display and data persistence.
 * @param timeLimitSeconds The number of seconds the user has to answer.
 */
data class GeneratedQuestion(
    val questionText: String,
    val answers: List<String>,
    val checkAnswer: (userAnswer: String) -> Boolean,
    val correctAnswer: String,
    val timeLimitSeconds: Int
)

/*
 * NOTE: If we choose to add more question formats, we need to make this class abstract,
 *  move the "answers" variable to a subclass, and add a "format" variable. We'll also
 *  need to ass a "format" field to the QuestionInstance class
 */