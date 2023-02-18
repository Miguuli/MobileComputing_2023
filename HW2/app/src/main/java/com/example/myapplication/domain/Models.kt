package com.example.myapplication.domain

import com.example.myapplication.data.entity.Message

data class MessageStore(
    val messages: List<Message> = emptyList(),
    val selected_message: Message? = null
)