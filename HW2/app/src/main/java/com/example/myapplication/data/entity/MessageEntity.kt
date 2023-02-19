package com.example.myapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.random.Random

@Entity
data class Message(
    @PrimaryKey val uid: Long,
    @ColumnInfo(name = "content") var content: String?,
    @ColumnInfo(name = "location_x") val locationX: Long = Random.nextLong(),
    @ColumnInfo(name = "location_y") val locationY: Float = Random.nextFloat(),
    @ColumnInfo(name= "reminder_time") val reminderTime: Long = Random.nextLong(),
    @ColumnInfo(name= "creation_time") val creationTime: Long = Date().time,
    @ColumnInfo(name = "reminder_seen") val reminderSeen: Long = Random.nextLong()
)