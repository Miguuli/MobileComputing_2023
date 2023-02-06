package com.example.myapplication.viewmodels

import android.app.Application
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.domain.Message

class MessageViewModel(private val app: Application): ViewModel(){
    fun addMessage(message: String) {
        _messages.add(message)
    }

    private val _messages = Message().message_list.toMutableStateList()
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