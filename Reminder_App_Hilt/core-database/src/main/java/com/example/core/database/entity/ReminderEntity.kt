package com.example.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date
import kotlin.random.Random

@Entity(
    tableName = "reminders",
    indices = [Index("uid", unique = true)]
)
data class ReminderEntity(
    @PrimaryKey val uid: Long,
    @ColumnInfo(name = "content") var content: String?,
    @ColumnInfo(name= "reminder_time") var reminderTime: String = String(),
    @ColumnInfo(name= "creation_time") var creationTime: Long = Date().time,
    @ColumnInfo(name = "reminder_seen") val reminderSeen: Long = Random.nextLong(),
    @ColumnInfo(name = "enabled") var enabled: Boolean = false
)
