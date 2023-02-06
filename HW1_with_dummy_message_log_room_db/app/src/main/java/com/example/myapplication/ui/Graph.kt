package com.example.myapplication.ui

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.room.UserDatabase
import com.example.myapplication.data.repository.UserRepository

object Graph {
    lateinit var userDatabase: UserDatabase

    val userRepository by lazy {
        UserRepository(
            userDao = userDatabase.userDao()
        )
    }

    fun provide(context: Context?){
        userDatabase = Room.databaseBuilder(context!!, UserDatabase::class.java, "myApplicationData.db")
            .fallbackToDestructiveMigration()
            .enableMultiInstanceInvalidation()
            .build()
    }
}