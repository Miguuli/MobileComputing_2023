package com.example.myapplication.ui

import android.app.Application

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Graph.provide(this.applicationContext)
        println("MyApplication() created")
    }
}