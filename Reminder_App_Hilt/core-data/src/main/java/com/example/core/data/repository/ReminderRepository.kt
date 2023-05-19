package com.example.core.data.repository

import com.example.core.model.entity.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    suspend fun reminders(): Flow<List<Reminder>>
    suspend fun addReminder(reminder: Reminder): Long
    suspend fun editReminder(reminder: Reminder): Long
    suspend fun editReminderVisibility(reminder: Reminder): Long
    suspend fun editAll(enabled: Boolean)
    suspend fun deleteReminder(uid: Long): Int
}