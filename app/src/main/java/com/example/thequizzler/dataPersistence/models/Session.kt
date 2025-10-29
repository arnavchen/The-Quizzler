package com.example.thequizzler.dataPersistence.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "user_name")
    val userName: String,

    @ColumnInfo(name = "start_time")
    val startTime: Long,

    val score: Int
)
