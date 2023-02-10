package com.example.myapplication.data.repository

import com.example.myapplication.data.entity.Message
import com.example.myapplication.data.room.MessageDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MessageRepository(private val messageDao: MessageDao){
    fun messages(): Flow<List<Message>> = messageDao.messages()
    fun getMessageWithId(uID: Long): Message? = messageDao.getMessageWithId(uID)

    private val my_job = SupervisorJob()
    private val my_scope = CoroutineScope(my_job + Dispatchers.IO)

    /**
     * Add a message to the database if it does not exist
     *
     * @return the id of the newly added/created message
     */

    fun addMessage(message: Message): Long {
        var ret = 0L
        my_scope.launch {
            ret = when (val local = messageDao.getMessageWithId(message.uid)) {
                null -> messageDao.insert(message)
                else -> local.uid
            }
        }
        return ret
    }

    fun editMessage(message: Message): Long {
        var ret = 0L
        my_scope.launch {
            when (val local = messageDao.getMessageWithId(message.uid)) {
                null -> messageDao.update(message)
                else -> ret = local.uid
            }
        }
        return ret
    }
}