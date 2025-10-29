package com.example.thequizzler.dataPersistence.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.thequizzler.dataPersistence.models.QuestionInstance
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionInstanceDao {

    /**
     * Inserts a question instance into the table.
     * @param questionInstance The question instance object to insert.
     */
    @Insert
    suspend fun insertQuestion(questionInstance: QuestionInstance)

    /**
     * Inserts a list of question instances, typically used to save a whole quiz at once.
     * @param questions The list of question instances to insert.
     */
    @Insert
    suspend fun insertAllQuestions(questions: List<QuestionInstance>)

    /**
     * Retrieves all question instances associated with a specific session ID.
     * @param sessionId The foreign key of the session.
     * @return A Flow emitting a list of QuestionInstance objects for the given session.
     */
    @Query("SELECT * FROM question_instances WHERE session_id = :sessionId ORDER BY q_number ASC")
    fun getQuestionsForSession(sessionId: Int): Flow<List<QuestionInstance>>
}
