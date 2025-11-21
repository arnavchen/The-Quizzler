package com.example.thequizzler.quiz

import com.example.thequizzler.quiz.templates.QuestionGenerationContext
import com.example.thequizzler.quiz.templates.SimpleLocation
import com.example.thequizzler.quiz.templates.TemplatesRegistry
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Responsible for generating a list of quiz questions based on provided settings.
 * This class abstracts away the details of how questions are created (e.g., from templates).
 *
 * @param settings The quiz settings, determining if online features are used.
 * @param services A container of repositories to use for generating questions
 */
class QuestionGenerator(
    private val settings: QuizSettings,
    private val services: QuestionServices
) {
    /**
     * Generates a list of unique quiz questions.
     *
     * @param count The desired number of questions.
     * @param location The user's current location, if available.
     * @return A list of [GeneratedQuestion] objects.
     */
    suspend fun generateQuestions(count: Int, location: SimpleLocation?): List<GeneratedQuestion> {
        val questions = mutableListOf<GeneratedQuestion>()
        val mutex = Mutex() // To safely add questions from concurrent jobs

        // Use a fake repository if offline or location is disabled
        val servicesToUse = if (settings.isOfflineMode || !settings.isLocationEnabled) {
            QuestionServices(placesRepository = FakePlacesRepository())
        } else {
            services // Use the real services
        }

        // The context now gets the entire services object
        val context = QuestionGenerationContext(location, servicesToUse)

        val templates = TemplatesRegistry.allTemplates().shuffled()

        // Launch concurrent jobs to generate questions faster
        coroutineScope {
            for (template in templates) {
                if (questions.size >= count) break
                launch {
                    try {
                        val instance = template.generate(context)
                        if (instance != null) {
                            val generatedQuestion = GeneratedQuestion(
                                questionText = instance.questionText,
                                answers = instance.answers.shuffled(),
                                checkAnswer = { userAnswer -> userAnswer == instance.correctAnswer },
                                correctAnswer = instance.correctAnswer,
                                timeLimitSeconds = instance.timeLimitSeconds
                            )
                            mutex.withLock {
                                if (questions.size < count) {
                                    questions.add(generatedQuestion)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        // Ignore errors from individual templates
                    }
                }
            }
        }
        return questions
    }
}

/**
 * A simple data class to hold settings relevant to question generation.
 */
data class QuizSettings(
    val isOfflineMode: Boolean,
    val isLocationEnabled: Boolean,
    // Add other settings like measurement system here later
)