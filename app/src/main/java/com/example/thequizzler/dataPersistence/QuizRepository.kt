package com.example.thequizzler.dataPersistence

import com.example.thequizzler.dataPersistence.daos.HighScoreDao
import com.example.thequizzler.dataPersistence.daos.QuestionInstanceDao
import com.example.thequizzler.dataPersistence.daos.SessionDao
import com.example.thequizzler.dataPersistence.models.HighScore
import com.example.thequizzler.dataPersistence.models.QuestionInstance
import com.example.thequizzler.dataPersistence.models.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

/**
 * Repository class that acts as a single source of truth for app data.
 * It abstracts the data sources (in this case, the Room DAOs) from the rest of the app.
 *
 * @param sessionDao The Data Access Object for Session data.
 * @param questionInstanceDao The Data Access Object for QuestionInstance data.
 * @param highScoreDao The Data Access Object for HighScore data.
 */
class QuizRepository(
    private val sessionDao: SessionDao,
    private val questionInstanceDao: QuestionInstanceDao,
    private val highScoreDao: HighScoreDao
) {

    // --- Session Functions ---

    /**
     * Retrieves a session given its id
     */
    fun getSessionById(sessionId: Int): Flow<Session?> {
        return sessionDao.getSessionById(sessionId)
    }

    /**
     * Retrieves all sessions from the database, ordered by the most recent first.
     */
    val allSessions: Flow<List<Session>> = sessionDao.getAllSessions()

    /**
     * Inserts a new session and returns its newly generated ID.
     */
    suspend fun insertSession(session: Session): Long {
        return sessionDao.insertSession(session)
    }

    /**
     * Deletes a session given its id
     */
    suspend fun deleteSessionById(sessionId: Int) {
        sessionDao.deleteSessionById(sessionId)
    }

    /**
     * Deletes all sessions from the database. FOR TESTING PURPOSES ONLY.
     */
    suspend fun clearAllSessions() {
        sessionDao.clearAllSessions()
    }



    // --- QuestionInstance Functions ---

    /**
     * Inserts a list of question instances for a completed quiz.
     */
    suspend fun insertAllQuestions(questions: List<QuestionInstance>) {
        questionInstanceDao.insertAllQuestions(questions)
    }

    /**
     * Retrieves all the question instances for a specific session.
     */
    fun getQuestionsForSession(sessionId: Int): Flow<List<QuestionInstance>> {
        return questionInstanceDao.getQuestionsForSession(sessionId)
    }

    // --- High Score Functions ---

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
            // High score list has fewer than 10 entries, new score automatically qualifies.
            true
        } else {
            // High score list is full, check if new score is higher than the lowest.
            newSession.score > lowestHighScore.score
        }

        if (isHighScore) {
            // If the list was full, remove the old lowest score to make room.
            if (highScoresList.size >= 10 && lowestHighScore != null) {
                highScoreDao.deleteHighScoreBySessionId(lowestHighScore.id)
            }
            // Insert the new high score entry.
            highScoreDao.insertHighScore(HighScore(sessionId = newSession.id))
        }
    }
}
