package com.example.myapplication.ui

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.room.ReminderDatabase
import com.example.myapplication.data.repository.ReminderRepository

object Graph {
    lateinit var reminderDatabase: ReminderDatabase

    val reminderRepository by lazy {
        ReminderRepository(
            reminderDao = reminderDatabase.reminderDao()
        )
    }

    fun provide(context: Context?){
        reminderDatabase = Room.databaseBuilder(context!!, ReminderDatabase::class.java, "myApplicationData.db")
            .fallbackToDestructiveMigration()
            .enableMultiInstanceInvalidation()
            .build()
    }
}