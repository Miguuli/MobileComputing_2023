package com.example.myapplication.viewmodels

import android.app.Application
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.entity.Message
import com.example.myapplication.data.repository.MessageRepository
import com.example.myapplication.domain.DummyMessage
import com.example.myapplication.ui.Graph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.random.Random

class MessageViewModel(private val app: Application,
                       private val messageRepository: MessageRepository = Graph.messageRepository)
    : ViewModel(){
    private val my_job = SupervisorJob()
    private val my_scope = CoroutineScope(my_job + Dispatchers.Main)

    fun addMessage(message_content: String) {

        my_scope.launch {
            messageRepository.addMessage(
                Message(
                uid = Random.nextLong(),
                    creationTime = Random.nextLong(),
                    locationX = Random.nextFloat(),
                    locationY = Random.nextFloat(),
                    reminderSeen = Random.nextLong(),
                    reminderTime = Random.nextLong()
            ).also {
                _messages.add(message_content)
                }
            )
        }
    }

    fun editMessage(uID: Long) {
        my_scope.launch {
            messageRepository.editMessage(
                    message = messageRepository.getMessageWithId(uID)!!
            )
        }
    }

    fun removeMessage(index: Int) {
        _messages.removeAt(index)
    }
    fun updateEnable(flag: Boolean){
        enabled = flag
    }

    fun updateContent(message_content: String, index: Int) {
        _messages[index] = message_content
    }

    var enabled by  mutableStateOf(false)
        private set

    private val _messages = DummyMessage().message_list.toMutableStateList()
    val messages: SnapshotStateList<String>
        get() = _messages

    init {
        _messages.addAll(
            listOf("Hello1", "Hello2", "Hello3", "Hello4", "Hello5",
                "Hello6", "Hello7", "Hello8", "Hello9", "Hello10"))
    }
}

class MessageViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MessageViewModel(app) as T
    }
}