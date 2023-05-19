package com.example.core.data.repository

import com.example.core.data.datasource.reminder.ReminderDataSource
import com.example.core.model.entity.Reminder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
        private val dataSource: ReminderDataSource
): ReminderRepository {
    override suspend fun reminders(): Flow<List<Reminder>> {
        return dataSource.reminders()
    }

    override suspend fun addReminder(reminder: Reminder): Long {
        return dataSource.addReminder(reminder)
    }

    override suspend fun editReminder(reminder: Reminder): Long {
        return dataSource.editReminder(reminder)
    }

    override suspend fun editReminderVisibility(reminder: Reminder): Long {
        return dataSource.editReminderVisibility(reminder)
    }

    override suspend fun editAll(enabled: Boolean) {
        return dataSource.editAll(enabled)
    }

    override suspend fun deleteReminder(uid: Long): Int {
        return dataSource.deleteReminder(uid)
    }
}