package com.example.myapplication.viewmodels

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.entity.Message
import com.example.myapplication.data.repository.MessageRepository
import com.example.myapplication.domain.MessageStore
import com.example.myapplication.ui.Graph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

class MessageViewModel(private val app: Application,
                       private val messageRepository: MessageRepository = Graph.messageRepository)
    : ViewModel(){
    private val _selected_message = MutableStateFlow<Message?>(null)

    private val _state = MutableStateFlow(MessageStore())
    val state: StateFlow<MessageStore>
        get() = _state
    var enabled by  mutableStateOf(false)
        private set

    fun editMessage(uid: Long, message_content: String) {
        viewModelScope.launch {
            val message = Message(uid = uid, content = message_content)
            messageRepository.editMessage(message = message)
            println("edited_message: $message_content")
        }
    }


    fun addMessage(message_content: String){
        viewModelScope.launch {
            val message = Message(uid = Random.nextLong(), content = message_content,
            creationTime = Date().time)
            messageRepository.addMessage(message = message)
        }
    }

    fun removeMessage(uid: Long) {
        viewModelScope.launch {
            messageRepository.deleteMessage(uid = uid)
        }
    }

    fun updateEnable(flag: Boolean){
        enabled = flag
    }

    fun addDummyDataToDb(){
        val dummy_message_list = listOf(
            Message(uid = Random.nextLong(), content = "Hello1", creationTime = Date().time),
            Message(uid = 2, content = "Hello2", creationTime = Date().time),
            Message(uid = 3, content = "Hello3", creationTime = Date().time),
            Message(uid = 4, content = "Hello4", creationTime = Date().time),
            Message(uid = 5, content = "Hello5", creationTime = Date().time),
            Message(uid = 6, content = "Hello6", creationTime = Date().time),
            Message(uid = 7, content = "Hello7", creationTime = Date().time),
            Message(uid = 8, content = "Hello8", creationTime = Date().time),
            Message(uid = 9, content = "Hello9", creationTime = Date().time),
            Message(uid = 10, content = "Hello10", creationTime = Date().time)
        )
        viewModelScope.launch {
            dummy_message_list.forEach{
                    message-> messageRepository.addMessage(message)
            }
        }
    }

    init {
        viewModelScope.launch{
            combine(
                messageRepository.messages().onEach { list->
                    if (list.isNotEmpty() && _selected_message.value == null) {
                        _selected_message.value = list[0]
                    }
                },
                _selected_message
            ){ messages, selected_message->
                MessageStore(
                    messages = messages, selected_message = selected_message
                )
            }.collect{_state.value = it }
        }
        //addDummyDataToDb()
    }
}

class MessageViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MessageViewModel(app) as T
    }
}