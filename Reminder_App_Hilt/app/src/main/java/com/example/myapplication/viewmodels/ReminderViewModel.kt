package com.example.myapplication.viewmodels

import android.location.Location
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.businesslogic.reminder.AddReminderUseCase
import com.example.core.domain.businesslogic.reminder.GetRemindersUseCase
import com.example.core.model.entity.Reminder
import com.example.core.model.entity.ReminderStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val getRemindersUseCase: GetRemindersUseCase,
    private val addReminderUseCase: AddReminderUseCase
)
: ViewModel(){

    private val _selected_reminder = MutableStateFlow<Reminder?>(null)

    private val _state = MutableStateFlow(ReminderStore())
    val state: StateFlow<ReminderStore>
        get() = _state
    var enabled by  mutableStateOf(false)
        private set

    fun addReminder(reminderTime: String,
                    message_content: String){
        viewModelScope.launch{
            addReminderUseCase(Reminder(
                content = message_content,
                reminderTime = reminderTime)
            )
        }
    }

    init {
        viewModelScope.launch{
            getRemindersUseCase().collect{
              _state.value = it
            }
        }
    }
}

