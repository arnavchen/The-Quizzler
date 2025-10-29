package com.example.thequizzler.dataPersistence.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "question_instances",
    foreignKeys = [
        ForeignKey(
            entity = Session::class,
            parentColumns = ["id"],
            childColumns = ["session_id"],
            onDelete = ForeignKey.CASCADE // QuestionInstance deleted if Session is
        )
    ]
)
data class QuestionInstance(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "session_id", index = true)
    val sessionId: Int,

    @ColumnInfo(name = "q_number")
    val questionNumber: Int,

    val points: Int,

    val question: String,

    @ColumnInfo(name = "user_response")
    val userResponse: String,

    @ColumnInfo(name = "was_correct")
    val wasCorrect: Boolean,

    @ColumnInfo(name = "correct_response")
    val correctResponse: String
)
