package com.example.core.domain.businesslogic.reminder

import com.example.core.data.repository.ReminderRepository
import com.example.core.model.entity.Reminder
import com.example.core.model.entity.ReminderStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetRemindersUseCase @Inject constructor(
    private val coroutinecontext: CoroutineContext,
    private val repository: ReminderRepository
) {
    suspend operator fun invoke() = withContext(coroutinecontext){
       repository.reminders().map {
               ReminderStore(reminders = it, null)
       }
    }
}

class AddReminderUseCase @Inject constructor(
    private val coroutinecontext: CoroutineContext,
    private val repository: ReminderRepository) {

    suspend operator fun invoke(reminder: Reminder): Long
    = withContext(coroutinecontext){
        repository.addReminder(reminder)
    }
}

class EditReminderUseCase @Inject constructor(
    private val coroutinecontext: CoroutineContext,
    private val repository: ReminderRepository) {

    suspend operator fun invoke(reminder: Reminder): Long
    = withContext(coroutinecontext){
        repository.editReminder(reminder)
    }
}

class DeleteReminderUseCase @Inject constructor(
    private val coroutinecontext: CoroutineContext,
    private val repository: ReminderRepository) {

    suspend operator fun invoke(uid: Long)
    = withContext(coroutinecontext){
        repository.deleteReminder(uid)
    }
}