package com.example.myapplication.viewmodels

import android.app.PendingIntent
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.businesslogic.reminder.AddReminderUseCase
import com.example.core.domain.businesslogic.reminder.DeleteReminderUseCase
import com.example.core.domain.businesslogic.reminder.EditReminderUseCase
import com.example.core.domain.businesslogic.reminder.GetRemindersUseCase
import com.example.core.model.entity.Reminder
import com.example.core.model.entity.ReminderStore
import com.example.myapplication.Graph
import com.example.myapplication.tools.setEditNotification
import com.example.myapplication.tools.setNotifications
import com.example.myapplication.tools.setRemoveNotification
import com.example.myapplication.ui.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val getRemindersUseCase: GetRemindersUseCase,
    private val addReminderUseCase: AddReminderUseCase,
    private val editReminderUseCase: EditReminderUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase
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
            val reminder = Reminder(
                content = message_content,
                reminderTime = reminderTime)

            val intent = PendingIntent.getActivity(Graph.appContext, 0,
                Intent(Graph.appContext, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
            val ret = addReminderUseCase(reminder)
            println("addReminderUseCase: returned $ret")

            setNotifications(reminder.uid, reminder.reminderTime, reminder.content,
                intent)
        }
    }

    fun editReminder(time: String, message_content: String, uid: Long){
        viewModelScope.launch {
            val reminder = Reminder(
                uid = uid, content = message_content, reminderTime = time
            )
            val intent = PendingIntent.getActivity(Graph.appContext, 0,
                Intent(Graph.appContext, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
            val ret = editReminderUseCase(reminder)
            println("editReminderUseCase: returned $ret")
            setEditNotification(reminder.uid, reminder.reminderTime, reminder.content, intent)
        }
    }

    fun removeReminder(uid: Long) {
        viewModelScope.launch {
            val intent = PendingIntent.getActivity(Graph.appContext, 0,
                Intent(Graph.appContext, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
            val ret = deleteReminderUseCase(uid)
            println("deleteReminderUseCase: returned $ret")
            setRemoveNotification(uid, intent)
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
