package com.example.myapplication.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.entity.Message

/**
 * The [RoomDatabase] for this app
 */
@Database(
    entities = [Message::class],
    version = 1,
    exportSchema = false
)
abstract class MessageDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}