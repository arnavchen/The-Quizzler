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
 */
@Database(
    entities = [Session::class, QuestionInstance::class, HighScore::class],
    version = 2,
    exportSchema = false
)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun sessionDao(): SessionDao
    abstract fun questionInstanceDao(): QuestionInstanceDao
    abstract fun highScoreDao(): HighScoreDao

    companion object {
        @Volatile
        private var INSTANCE: QuizDatabase? = null

        /**
         * Gets the singleton instance of the database.
         *
         * @param context The application context.
         * @return The singleton QuizzlerDatabase instance.
         */
        fun getDatabase(context: Context): QuizDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuizDatabase::class.java,
                    "quizzler_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
