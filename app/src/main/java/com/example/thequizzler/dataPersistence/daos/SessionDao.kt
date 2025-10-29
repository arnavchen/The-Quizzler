package com.example.thequizzler.dataPersistence.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.thequizzler.dataPersistence.models.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    /**
     * Inserts a new session into the table. This returns the new row's ID as a Long.
     * @param session The session object to insert.
     * @return The id of the newly inserted session.
     */
    @Insert
    suspend fun insertSession(session: Session): Long

    /**
     * Retrieves a single session by its ID.
     * @param sessionId The ID of the session to retrieve.
     * @return A Flow emitting the Session object, or null if not found.
     */
    @Query("SELECT * FROM sessions WHERE id = :sessionId")
    fun getSessionById(sessionId: Int): Flow<Session?>

    /**
     * Retrieves all sessions from the table, ordered by the most recent start time first.
     * @return A Flow emitting a list of all Session objects.
     */
    @Query("SELECT * FROM sessions ORDER BY start_time DESC")
    fun getAllSessions(): Flow<List<Session>>

    /**
     * Deletes a session from the table by its id
     * @param sessionId The ID of the session to delete.
     */
    @Query("DELETE FROM sessions WHERE id = :sessionId")
    suspend fun deleteSessionById(sessionId: Int)


    /**
     * Deletes all sessions from the table.
     */
    @Query("DELETE FROM sessions")
    suspend fun clearAllSessions()
}
