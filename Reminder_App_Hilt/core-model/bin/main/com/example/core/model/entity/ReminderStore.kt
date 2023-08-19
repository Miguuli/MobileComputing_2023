package com.example.core.model.entity

data class ReminderStore(
    val reminders: List<Reminder> = emptyList(),
    val selected_reminder: Reminder? = null
)
