package com.example.myapplication.domain

data class Message(val id: Int = 0,
                  ){
    val message_list = mutableListOf<String>()
}