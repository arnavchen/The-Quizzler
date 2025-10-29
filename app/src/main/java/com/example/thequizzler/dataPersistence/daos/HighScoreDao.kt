package com.example.thequizzler.dataPersistence.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.thequizzler.dataPersistence.models.HighScore
import com.example.thequizzler.dataPersistence.models.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface HighScoreDao {

    /**
     * Inserts a new high score entry.
     * @param highScore The HighScore object to insert.
     */
    @Insert
    suspend fun insertHighScore(highScore: HighScore)

    /**
     * Gets the list of sessions that are marked as high scores, ordered from highest score to lowest.
     * This query joins the high_scores table with the sessions table to retrieve the full session details.
     * We can also add a LIMIT clause to control how many high scores are returned.
     * @return A Flow emitting a list of the top Session objects.
     */
    @Query("SELECT s.* FROM sessions s INNER JOIN high_scores hs ON s.id = hs.session_id ORDER BY s.score DESC LIMIT 10")
    fun getHighScores(): Flow<List<Session>>

    /**
     * Gets the lowest score from the current list of high scores.
     * This is useful for checking if a new score is high enough to make the list.
     * Returns a single Session object or null if the table is empty.
     */
    @Query("SELECT s.* FROM sessions s INNER JOIN high_scores hs ON s.id = hs.session_id ORDER BY s.score ASC LIMIT 1")
    fun getLowestHighScore(): Flow<Session?>

    /**
     * Deletes a high score entry by its session ID. This is useful for removing the lowest
     * high score when a new, higher score replaces it.
     * @param sessionId The ID of the session to remove from the high scores list.
     */
    @Query("DELETE FROM high_scores WHERE session_id = :sessionId")
    suspend fun deleteHighScoreBySessionId(sessionId: Int)
}
