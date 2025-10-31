package com.example.thequizzler.dataPersistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.thequizzler.dataPersistence.daos.HighScoreDao
import com.example.thequizzler.dataPersistence.daos.QuestionInstanceDao
import com.example.thequizzler.dataPersistence.daos.SessionDao
import com.example.thequizzler.dataPersistence.models.HighScore
import com.example.thequizzler.dataPersistence.models.QuestionInstance
import com.example.thequizzler.dataPersistence.models.Session

/**
 * The Room database for the application.
 * This class defines the entities the database contains and provides access to the DAOs.
 */
@Database(
    entities = [Session::class, QuestionInstance::class, HighScore::class],
    version = 2,
    exportSchema = false
)
abstract class QuizDatabase : RoomDatabase() {

    // Abstract methods to get the DAOs for each entity. Room will generate the implementation.
    abstract fun sessionDao(): SessionDao
    abstract fun questionInstanceDao(): QuestionInstanceDao
    abstract fun highScoreDao(): HighScoreDao

    // Companion object to provide a singleton instance of the database.
    // This prevents having multiple instances of the database open at the same time.
    companion object {
        // The '@Volatile' annotation ensures that the INSTANCE variable is always up-to-date
        // and visible to all execution threads.
        @Volatile
        private var INSTANCE: QuizDatabase? = null

        /**
         * Gets the singleton instance of the database.
         *
         * @param context The application context.
         * @return The singleton QuizzlerDatabase instance.
         */
        fun getDatabase(context: Context): QuizDatabase {
            // Return the existing instance if it's not null.
            // If it is null, create the database in a synchronized block to ensure thread safety.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuizDatabase::class.java,
                    "quizzler_database" // This is the file name for your database.
                )
                    // You can add migrations here if you change the schema later.
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                // return the instance
                instance
            }
        }
    }
}
