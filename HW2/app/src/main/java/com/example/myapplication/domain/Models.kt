package com.example.myapplication.domain

data class DummyMessage(val id: Int = 0,
                  ){
    val message_list = mutableListOf<String>()
}