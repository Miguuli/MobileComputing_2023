package com.example.myapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message(
    @PrimaryKey val uid: Long,
    @ColumnInfo(name = "location_x") val locationX: Float?,
    @ColumnInfo(name = "location_y") val locationY: Float?,
    @ColumnInfo(name= "reminder_time") val reminderTime: Long?,
    @ColumnInfo(name= "creation_time") val creationTime: Long?,
    @ColumnInfo(name = "reminder_seen") val reminderSeen: Long?
)