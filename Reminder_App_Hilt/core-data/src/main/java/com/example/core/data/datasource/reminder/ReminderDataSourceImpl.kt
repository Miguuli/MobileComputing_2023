package com.example.core.data.datasource.reminder

import com.example.core.database.dao.ReminderDao
import com.example.core.database.entity.ReminderEntity
import com.example.core.model.entity.Reminder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Array

class ReminderDataSourceImpl @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderDataSource {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override suspend fun reminders(): Flow<List<Reminder>> {
        return reminderDao.reminders().map { list ->
            List(list.size) { index ->
                Reminder(
                    list[index].uid, list[index].content,
                    list[index].reminderTime, list[index].creationTime
                )
            }
        }
    }

    override suspend fun addReminder(reminder: Reminder): Long {
        var ret = 0L
        scope.launch {
            ret = when (val local = reminderDao.getReminderWithId(reminder.uid)) {
                null -> reminderDao.insert(ReminderEntity(
                    reminder.uid, reminder.content,
                    reminder.reminderTime, reminder.creationTime,
                    reminder.reminderSeen, reminder.enabled)
                )
                else -> local.uid
            }
        }
        return ret
    }

    override suspend fun editReminder(reminder: Reminder): Long {
        var ret = 0L
        scope.launch {
            ret = when(reminderDao.getReminderWithId(reminder.uid)) {
                null-> ret
                else-> {
                    reminderDao.update(ReminderEntity(reminder.uid, reminder.content,
                        reminder.reminderTime, reminder.creationTime,
                        reminder.reminderSeen, reminder.enabled)
                    )
                    1
                }
            }
        }
        return ret
    }

    override suspend fun editReminderVisibility(reminder: Reminder): Long {
        var ret = 0L
        scope.launch {
            ret = when(reminderDao.getReminderWithId(reminder.uid)) {
                null-> ret
                else-> {
                    reminderDao.update(ReminderEntity(reminder.uid, reminder.content,
                        reminder.reminderTime, reminder.creationTime,
                        reminder.reminderSeen, reminder.enabled)
                    )
                    1
                }
            }
        }
        return ret
    }

    override suspend fun editAll(enabled: Boolean) {
        scope.launch {
            reminderDao.reminders().collect {reminders_from_db->
                reminders_from_db.forEach{reminder_from_db->
                    val reminder = ReminderEntity(
                        uid = reminder_from_db.uid,
                        content = reminder_from_db.content,
                        reminderTime = reminder_from_db.reminderTime,
                        enabled = enabled
                    )
                    reminderDao.update(reminder)
                }
            }
        }
    }

    override suspend fun deleteReminder(uid: Long): Int {
        var ret = 0
        scope.launch {
            ret = when (val local = reminderDao.getReminderWithId(uid)) {
                null-> ret
                else->{
                    reminderDao.delete(local)
                }
            }
        }
        return ret
    }
}