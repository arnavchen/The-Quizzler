package com.example.thequizzler.quiz.templates

import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.random.Random

/**
 * Registry that exposes a set of in-memory templates you can use to generate a quiz.
 * These are intentionally conservative and will fall back to mock data when location
 * or places data is unavailable.
 */
object TemplatesRegistry {
    /**
     * Returns a list of templates (10–12) covering a range of question types. These are
     * parameterized instances of two small template classes (WhichIsCloser / DistanceToNearest).
     */
    fun allTemplates(): List<QuestionTemplate> {
        return listOf(
            WhichIsCloserTemplate(
                id = "which_closer_brands",
                candidateKeywords = listOf("Starbucks", "McDonald's", "Chipotle", "Chick-fil-A")
            ),
            WhichIsCloserTemplate(
                id = "which_closer_food",
                candidateKeywords = listOf("Pizza Hut", "Subway", "Panda Express", "Taco Bell")
            ),
            DistanceToNearestTemplate(id = "distance_starbucks", keyword = "Starbucks"),
            DistanceToNearestTemplate(id = "distance_park", keyword = "park"),
            DistanceToNearestTemplate(id = "distance_pharmacy", keyword = "pharmacy"),
            WhichIsCloserTemplate(id = "which_closer_transport", candidateKeywords = listOf("Train station", "Airport", "Bus stop", "Ferry")),
            DistanceToNearestTemplate(id = "distance_hospital", keyword = "hospital", defaultTimeSeconds = 20),
            WhichIsCloserTemplate(id = "which_closer_shopping", candidateKeywords = listOf("Mall", "Grocery store", "Bank", "Library")),
            DistanceToNearestTemplate(id = "distance_gas", keyword = "gas station"),
            DistanceToNearestTemplate(id = "distance_national_park", keyword = "national park", defaultTimeSeconds = 30),
            WhichIsCloserTemplate(id = "which_closer_cafes", candidateKeywords = listOf("Coffee shop", "Bakery", "Diner", "Tea house")),
            DistanceToNearestTemplate(id = "distance_museum", keyword = "museum")
        )
    }
}

/**
 * A simple template that asks "Which is closest?" among a set of keywords.
 */
class WhichIsCloserTemplate(
    override val id: String,
    override val displayName: String = "Which is closest?",
    override val defaultTimeSeconds: Int = 20,
    private val candidateKeywords: List<String>
) : QuestionTemplate {
    override suspend fun generate(context: QuestionGenerationContext): QuestionInstance? {
        // If no location, fallback to mock
        val loc = context.location
        val results = mutableListOf<Pair<String, Int>>() // name -> distance

        if (loc != null) {
            for (kw in candidateKeywords) {
                val r = context.placesRepo.findNearestByKeyword(kw, loc.latitude, loc.longitude)
                if (r != null) {
                    val dist = r.distanceMeters ?: estimateDistanceFromName(r.name, loc.latitude, loc.longitude)
                    results += r.name to dist
                }
                // keep fast and polite to remote services
                delay(20)
            }
        }

        if (results.size < 2) {
            // fallback mock: use keywords themselves as answers
            val choices = candidateKeywords.shuffled().take(4)
            return QuestionInstance(id, "Which is closest to you right now?", choices, choices.first(), defaultTimeSeconds)
        }

        val sorted = results.sortedBy { it.second }
        val correct = sorted.first()
        val answerChoices = results.map { it.first }.distinct().shuffled().take(4)

        return QuestionInstance(
            id = id,
            questionText = "Which is closest to you right now?",
            answers = if (answerChoices.size >= 4) answerChoices else (answerChoices + candidateKeywords).distinct().take(4),
            correctAnswer = correct.first,
            timeLimitSeconds = defaultTimeSeconds
        )
    }

    private fun estimateDistanceFromName(name: String, lat: Double, lng: Double): Int {
        // deterministic pseudo-estimate so fake repo and real repo behave similarly in tests
        val seed = abs((name + lat.toString() + lng.toString()).hashCode())
        return 50 + (seed % 2000)
    }
}

/**
 * A template that asks "How far is the nearest <keyword>?" and creates plausible distractors.
 */
class DistanceToNearestTemplate(
    override val id: String,
    override val displayName: String = "How far is the nearest",
    override val defaultTimeSeconds: Int = 18,
    private val keyword: String
) : QuestionTemplate {
    override suspend fun generate(context: QuestionGenerationContext): QuestionInstance? {
        val loc = context.location
        val nearest = if (loc != null) context.placesRepo.findNearestByKeyword(keyword, loc.latitude, loc.longitude) else null

        if (nearest == null) {
            // fallback mock values
            val correct = formatDistanceMeters(120)
            val others = listOf(formatDistanceMeters(50), formatDistanceMeters(300), formatDistanceMeters(1000)).shuffled()
            val options = (listOf(correct) + others).take(4).shuffled()
            return QuestionInstance(id, "How far is the nearest $keyword from you right now?", options, correct, defaultTimeSeconds)
        }

        val d = nearest.distanceMeters ?: estimateDistanceFromName(nearest.name, loc!!.latitude, loc.longitude)
        val correctStr = formatDistanceMeters(d)

        // generate three distractors that are plausible and distinct
        val distractors = mutableSetOf<String>()
        val rnd = Random(d)
        while (distractors.size < 3) {
            val offset = when (rnd.nextInt(3)) {
                0 -> (d + rnd.nextInt(50, 300))
                1 -> (d - rnd.nextInt(30, (d / 4).coerceAtLeast(30))).coerceAtLeast(10)
                else -> (d + rnd.nextInt(400, 2000))
            }
            distractors += formatDistanceMeters(offset)
        }

        val options = (listOf(correctStr) + distractors.toList()).shuffled()
        return QuestionInstance(id, "How far is the nearest ${nearest.name} from you right now?", options, correctStr, defaultTimeSeconds)
    }

    private fun estimateDistanceFromName(name: String, lat: Double, lng: Double): Int {
        val seed = abs((name + lat.toString() + lng.toString()).hashCode())
        return 100 + (seed % 5000)
    }

    private fun formatDistanceMeters(meters: Int): String {
        return if (meters >= 1000) String.format("%.1f km", meters / 1000.0) else "$meters m"
    }
}

/**
 * A tiny fake repository used for local development and tests — deterministic but synthetic.
 * It does not call any network services and is safe to use without API keys.
 */
class FakePlacesRepository : PlacesRepository {
    override suspend fun findNearestByKeyword(keyword: String, lat: Double, lng: Double): PlaceResult? {
        // Simulate a small delay to mimic IO
        delay(10)
        // Deterministic pseudo-random distance so tests are stable
        val seed = abs((keyword + lat.toString() + lng.toString()).hashCode())
        val dist = 30 + (seed % 7000) // 30m .. ~7km
        val name = "$keyword ${(seed % 5) + 1}"
        // place slightly offset from the user's lat/lng so distances appear realistic
        val latOffset = ((seed % 100) - 50) / 10000.0
        val lngOffset = (((seed / 31) % 100) - 50) / 10000.0
        return PlaceResult(name, lat + latOffset, lng + lngOffset, dist)
    }
}
