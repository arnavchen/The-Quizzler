package com.example.thequizzler.quiz.templates.locationTemplates.placesTemplates

import android.util.Log
import com.example.thequizzler.quiz.GeneratedQuestion
import com.example.thequizzler.quiz.QuestionServices
import com.example.thequizzler.quiz.QuizSettings
import com.example.thequizzler.quiz.apiRepositories.PlaceResult
import com.example.thequizzler.quiz.templates.IQuestionTemplate
import com.example.thequizzler.quiz.templates.SimpleLocation
import com.example.thequizzler.quiz.templates.TemplateRequirement
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.random.Random

object WhichPlaceIsCloserTemplate : IQuestionTemplate {

    override val requirements = setOf(TemplateRequirement.LOCATION, TemplateRequirement.INTERNET)

    override val weight = 3;

    private val franchises = PlacesKeywords.franchises
    private val placeTypes = PlacesKeywords.placeTypes

    override suspend fun generate(services: QuestionServices, location: SimpleLocation?, settings: QuizSettings): GeneratedQuestion? {

        if(location == null) {
            return null;
        }

        val useFranchises = Random.Default.nextBoolean()

        if (useFranchises) {
            val keywords = franchises.shuffled().take(4)
            Log.d("Keywords Chosen", keywords.toString());

            val results: List<PlaceResult> = coroutineScope {
                keywords.map { keyword ->
                    async {
                        services.placesRepository.findNearestPlaceByFranchiseName(
                            keyword, location.latitude, location.longitude
                        )
                    }
                }.awaitAll()
            }.filterNotNull()

            if (results.size < 2) return null

            val sorted = results.sortedBy { it.distanceMeters }
            val correctAnswer = sorted.first().name
            val answerChoices = results.map { it.name }.distinct().shuffled()

            if (answerChoices.size < 2) return null

            return GeneratedQuestion(
                questionText = "Which of these is closest to you right now?",
                answers = answerChoices,
                correctAnswer = correctAnswer,
                checkAnswer = { userAnswer -> userAnswer == correctAnswer },
                timeLimitSeconds = 15
            )

        } else {
            val chosenTypes = placeTypes.entries.shuffled().take(4)
            Log.d("Keywords Chosen", chosenTypes.map { it.value }.toString());

            val results: List<Pair<String, PlaceResult?>> = coroutineScope {
                chosenTypes.map { (prettyName, apiType) ->
                    async {
                        // Associate the pretty name with the API result
                        prettyName to services.placesRepository.findNearestPlaceByType(
                            apiType, location.latitude, location.longitude
                        )
                    }
                }.awaitAll()
            }

            // Filter out failures and create a list of (PrettyName, PlaceResult)
            val successfulResults = results.mapNotNull { (prettyName, placeResult) ->
                placeResult?.let { prettyName to it }
            }

            if (successfulResults.size < 4) {
                return null;
            }

            val sorted = successfulResults.sortedBy { it.second.distanceMeters }
            val correctAnswer = sorted.first().first
            val answerChoices = successfulResults.map { it.first }.distinct().shuffled()

            return GeneratedQuestion(
                questionText = "Which of these is closest to you right now?",
                answers = answerChoices,
                correctAnswer = correctAnswer,
                checkAnswer = { userAnswer -> userAnswer == correctAnswer },
                timeLimitSeconds = 15
            )
        }
    }
}