package com.example.myapplication.ui

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.room.MessageDatabase
import com.example.myapplication.data.repository.MessageRepository

object Graph {
    lateinit var messageDatabase: MessageDatabase

    val messageRepository by lazy {
        MessageRepository(
            messageDao = messageDatabase.messageDao()
        )
    }

    fun provide(context: Context?){
        messageDatabase = Room.databaseBuilder(context!!, MessageDatabase::class.java, "myApplicationData.db")
            .fallbackToDestructiveMigration()
            .enableMultiInstanceInvalidation()
            .build()
    }
}