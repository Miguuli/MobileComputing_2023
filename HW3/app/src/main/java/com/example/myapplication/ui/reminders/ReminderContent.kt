package com.example.myapplication.ui.reminders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.entity.Reminder
import com.example.myapplication.ui.theme.DeleteIcon
import com.example.myapplication.ui.theme.EditIcon
import com.example.myapplication.ui.theme.message_column_modifier
import com.example.myapplication.ui.theme.message_column_padding


@Composable
fun ReminderContentColumn(reminders: List<Reminder>,
                          onDeleteClick: (Long) -> Unit,
                          onEditMessage: (String, String, Long)->Unit){

    LazyColumn(contentPadding = message_column_padding,
        modifier = message_column_modifier,
        verticalArrangement = Arrangement.Center) {

        val items = reminders.sortedBy { message->message.creationTime }

        items.forEach{ message->
            if(message.enabled){
                item(key = message.creationTime){
                    ReminderRow(
                        message_content = message.content!!,
                        reminderTime = message.reminderTime,
                        onDeleteClick = { onDeleteClick(message.uid) },
                        onUpdateContent =  { time, content->onEditMessage(time, content, message.uid) }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }
}

@Composable
fun ReminderRow(
    message_content: String, reminderTime: String?,
    onDeleteClick: () -> Unit,
    onUpdateContent: (String, String) -> Unit) {

    LazyRow {
        item {
            ReminderContentFields(
                message_content = message_content,
                reminderTime = reminderTime
            )
        }
        item { DeleteIcon(onDeleteClick = onDeleteClick) }
        item {
            /**
             * Credit: https://www.codexpedia.com/android/android-jetpack-compose-show-alertdialog-on-button-click/
             */

            val showDialog = remember { mutableStateOf(false) }
            if (showDialog.value) {
                ReminderEditDialog(msg = "Edit reminder",
                    showDialog = showDialog.value,
                    onDismiss = {showDialog.value = false},
                    onEditMessage = {time, content-> onUpdateContent(time, content)})
            }
            EditIcon(onClick = {showDialog.value = true}) }
    }
}

@Composable
fun ReminderContentFields(message_content: String, reminderTime: String?){
    Column {
        Text("Time: $reminderTime")
        Text(message_content)
    }
}