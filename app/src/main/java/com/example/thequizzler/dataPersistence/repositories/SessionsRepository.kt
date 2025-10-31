package com.example.thequizzler.dataPersistence.repositories

import com.example.thequizzler.dataPersistence.daos.HighScoreDao
import com.example.thequizzler.dataPersistence.daos.QuestionInstanceDao
import com.example.thequizzler.dataPersistence.daos.SessionDao
import com.example.thequizzler.dataPersistence.models.Session
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that acts as a single source of truth for Session data.
 * It abstracts the Session DAO from the rest of the app.
 *
 * @param sessionDao The Data Access Object for Session data.
 */
class SessionsRepository(
    private val sessionDao: SessionDao,
) {

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
}
