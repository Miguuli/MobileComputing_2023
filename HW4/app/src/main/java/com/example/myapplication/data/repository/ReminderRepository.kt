package com.example.myapplication.data.repository

import com.example.myapplication.data.entity.Reminder
import com.example.myapplication.data.room.ReminderDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ReminderRepository(private val reminderDao: ReminderDao){
    fun reminders(): Flow<List<Reminder>> = reminderDao.reminders()

    private val my_job = SupervisorJob()
    private val my_scope = CoroutineScope(my_job + Dispatchers.IO)

    /**
     * Add a message to the database if it does not exist
     *
     * @return the id of the newly added/created message
     */

    fun addReminder(reminder: Reminder): Long {
        var ret = 0L
        my_scope.launch {
            ret = when (val local = reminderDao.getReminderWithId(reminder.uid)) {
                null -> reminderDao.insert(reminder)
                else -> local.uid
            }
        }
        return ret
    }

    fun editReminder(reminder: Reminder): Long {
        var ret = 0L
        my_scope.launch {
            ret = when(reminderDao.getReminderWithId(reminder.uid)) {
                null-> ret
                else-> {
                    reminderDao.update(reminder)
                    1
                }
            }
        }
        return ret
    }
    fun editReminderVisibility(reminder: Reminder): Long {
        var ret = 0L
        my_scope.launch {
            ret = when(reminderDao.getReminderWithId(reminder.uid)) {
                null-> ret
                else-> {
                    reminderDao.update(reminder)
                    1
                }
            }
        }
        return ret
    }

    fun editAll(enabled: Boolean) {

        my_scope.launch {
            reminderDao.reminders().collect {reminders_from_db->
                reminders_from_db.forEach{reminder_from_db->
                    val reminder = Reminder(
                        locationX = 0.0,
                        locationY = 0.0,
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

    fun deleteReminder(uid: Long): Int {
        var ret = 0
        my_scope.launch {
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