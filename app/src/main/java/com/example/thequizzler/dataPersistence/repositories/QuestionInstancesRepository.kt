package com.example.thequizzler.dataPersistence.repositories

import com.example.thequizzler.dataPersistence.daos.HighScoreDao
import com.example.thequizzler.dataPersistence.daos.QuestionInstanceDao
import com.example.thequizzler.dataPersistence.daos.SessionDao
import com.example.thequizzler.dataPersistence.models.QuestionInstance
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that acts as a single source of truth for QuestionInstance.
 * It abstracts the QuestoinInstance DAO from the rest of the app.
 *
 * @param questionInstanceDao The Data Access Object for QuestionInstance data.
 */
class QuestionInstancesRepository(
    private val questionInstanceDao: QuestionInstanceDao,
) {

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
}
