package com.example.myapplication.ui

import android.app.Application

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        println("MyApplication() created")
    }
}