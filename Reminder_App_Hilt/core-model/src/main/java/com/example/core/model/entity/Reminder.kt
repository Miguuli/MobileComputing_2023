package com.example.core.model.entity

import java.util.Date
import kotlin.random.Random
import kotlin.random.nextLong

data class Reminder(
    val uid: Long = Random.nextLong(),
    var content: String?,
    var reminderTime: String = String(),
    var creationTime: Long = Date().time,
    val reminderSeen: Long = Random.nextLong(),
    var enabled: Boolean = false
)
