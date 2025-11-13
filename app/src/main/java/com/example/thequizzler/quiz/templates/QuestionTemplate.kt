package com.example.thequizzler.quiz.templates

/**
 * Template interface for generating questions. Implementations should return null when
 * they cannot produce a valid question (e.g., missing data) — caller may fall back.
 */
interface QuestionTemplate {
    val id: String
    val displayName: String
    val defaultTimeSeconds: Int

    suspend fun generate(context: QuestionGenerationContext): QuestionInstance?
}
