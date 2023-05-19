package com.example.core.domain.businesslogic.reminder

import com.example.core.data.repository.ReminderRepository
import com.example.core.model.entity.Reminder
import com.example.core.model.entity.ReminderStore
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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
    private val repository: ReminderRepository
) {
    suspend operator fun invoke(reminder: Reminder) = withContext(coroutinecontext){
        repository.addReminder(reminder)
    }
}

/*
   fun editReminder(time: String, message_content: String,  uid: Long) {
       viewModelScope.launch {
           val reminder = Reminder(uid = uid, reminderTime = time, content = message_content,
               locationX = 0.0,
               locationY = 0.0)
           reminderRepository.editReminder(reminder = reminder)
           setEditNotification(reminder.uid, reminder.reminderTime, reminder.content)
           println("edited_message: $message_content")
       }
   }

   fun editReminderVisibility(
       uid: Long,
       reminderTime: String,
       enabled: Boolean,
       content: String,
       locationX: Double?,
       locationY: Double?
   ) {
       viewModelScope.launch {
           val reminder = Reminder(uid = uid, reminderTime = reminderTime , content = content, enabled = enabled,
               locationX = locationX,
               locationY = locationY)
           reminderRepository.editReminderVisibility(reminder = reminder)
           println("reminder visibility: $enabled")
       }
   }

   fun addReminder(location: String, message_content: String, reminderTime: String){
       viewModelScope.launch {
           if(reminderTime.isEmpty()) return@launch

           val destination_locationX = getDestinationLocationX(location)
           val destination_locationY = getDestinationLocationY(location)

           println("location: $location")
           val reminder = Reminder(
               locationX = destination_locationX,
               locationY = destination_locationY,
               uid = Random.nextLong(), content = message_content,
               creationTime = Date().time, reminderTime = reminderTime
           )
           reminderRepository.addReminder(reminder = reminder)
           setNotifications(reminder.uid, reminder.reminderTime, reminder.content,
               reminder.locationX, reminder.locationY)
       }
   }

   fun removeReminder(uid: Long) {
       viewModelScope.launch {
           reminderRepository.deleteReminder(uid = uid)
           setRemoveNotification(uid)
       }
   }
*/