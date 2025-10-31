package com.example.thequizzler.dataPersistence.repositories

import com.example.thequizzler.dataPersistence.daos.HighScoreDao
import com.example.thequizzler.dataPersistence.daos.QuestionInstanceDao
import com.example.thequizzler.dataPersistence.daos.SessionDao
import com.example.thequizzler.dataPersistence.models.HighScore
import com.example.thequizzler.dataPersistence.models.QuestionInstance
import com.example.thequizzler.dataPersistence.models.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

/**
 * Repository class that acts as a single source of truth for HighScore data.
 * It abstracts the data HighScore DAO from the rest of the app.
 *
 * @param highScoreDao The Data Access Object for HighScore data.
 */
class HighScoresRepository(
    private val highScoreDao: HighScoreDao
) {

    /**
     * Retrieves the list of top 10 scoring sessions.
     */
    val highScores: Flow<List<Session>> = highScoreDao.getHighScores()

    /**
     * Submits a new score, checks if it qualifies as a high score, and updates the
     * high_scores table accordingly, maintaining a maximum of 10 entries.
     *
     * @param newSession The session containing the new score to evaluate.
     */
    suspend fun submitNewHighScore(newSession: Session) {
        val lowestHighScore = highScoreDao.getLowestHighScore().firstOrNull()
        val highScoresList = highScores.firstOrNull() ?: emptyList()

        val isHighScore = if (lowestHighScore == null) {
            // High score list is empty, any score is a high score.
            true
        } else if (highScoresList.size < 10) {
            true
        } else {
            // High score list is full, check if new score is higher than the lowest.
            newSession.score > lowestHighScore.score
        }

        if (isHighScore) {
            if (highScoresList.size >= 10) {
                highScoreDao.deleteHighScoreBySessionId(lowestHighScore.id)
            }
            // Insert the new high score entry.
            highScoreDao.insertHighScore(HighScore(sessionId = newSession.id))
        }
    }
}
