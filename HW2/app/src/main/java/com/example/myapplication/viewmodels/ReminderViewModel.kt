package com.example.myapplication.viewmodels

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.entity.Reminder
import com.example.myapplication.data.repository.ReminderRepository
import com.example.myapplication.domain.ReminderStore
import com.example.myapplication.ui.Graph
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class ReminderViewModel(private val app: Application,
                        private val reminderRepository: ReminderRepository = Graph.reminderRepository)
    : ViewModel(){
    private val _selected_reminder = MutableStateFlow<Reminder?>(null)

    private val _state = MutableStateFlow(ReminderStore())
    val state: StateFlow<ReminderStore>
        get() = _state
    var enabled by  mutableStateOf(false)
        private set

    fun editReminder(uid: Long, message_content: String) {
        viewModelScope.launch {
            val reminder = Reminder(uid = uid, content = message_content)
            reminderRepository.editReminder(reminder = reminder)
            println("edited_message: $message_content")
        }
    }


    fun addReminder(message_content: String, reminderTime: String){
        viewModelScope.launch {
            val reminder = Reminder(
                uid = Random.nextLong(), content = message_content,
            creationTime = Date().time, reminderTime = reminderTime)
            reminderRepository.addReminder(reminder = reminder)
        }
    }

    fun removeReminder(uid: Long) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(uid = uid)
        }
    }

    fun updateEnable(flag: Boolean){
        enabled = flag
    }

    fun addDummyDataToDb(){
        val dummy_reminder_list = listOf(
            Reminder(uid = Random.nextLong(), content = "Hello1", creationTime = Date().time),
            Reminder(uid = 2, content = "Hello2", creationTime = Date().time),
            Reminder(uid = 3, content = "Hello3", creationTime = Date().time),
            Reminder(uid = 4, content = "Hello4", creationTime = Date().time),
            Reminder(uid = 5, content = "Hello5", creationTime = Date().time),
            Reminder(uid = 6, content = "Hello6", creationTime = Date().time),
            Reminder(uid = 7, content = "Hello7", creationTime = Date().time),
            Reminder(uid = 8, content = "Hello8", creationTime = Date().time),
            Reminder(uid = 9, content = "Hello9", creationTime = Date().time),
            Reminder(uid = 10, content = "Hello10", creationTime = Date().time)
        )
        viewModelScope.launch {
            dummy_reminder_list.forEach{
                    reminder-> reminderRepository.addReminder(reminder)
            }
        }
    }

    init {
        viewModelScope.launch{
            combine(
                reminderRepository.reminders().onEach { list->
                    if (list.isNotEmpty() && _selected_reminder.value == null) {
                        _selected_reminder.value = list[0]
                    }
                },
                _selected_reminder
            ){ messages, selected_message->
                ReminderStore(
                    reminders = messages, selected_reminder = selected_message
                )
            }.collect{_state.value = it }
        }
        //addDummyDataToDb()
    }
}

class ReminderViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReminderViewModel(app) as T
    }
}
