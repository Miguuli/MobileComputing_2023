package com.example.myapplication.domain

import com.example.myapplication.data.entity.Reminder

data class ReminderStore(
    val reminders: List<Reminder> = emptyList(),
    val selected_reminder: Reminder? = null
)