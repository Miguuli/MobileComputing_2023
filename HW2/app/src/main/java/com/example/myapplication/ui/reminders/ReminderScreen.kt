package com.example.myapplication.ui.reminders

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.entity.Reminder
import com.example.myapplication.ui.theme.*
import com.example.myapplication.viewmodels.ReminderViewModel
import com.example.myapplication.viewmodels.ReminderViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun ReminderScreen(app: Application,
                   onBackPress: () -> Unit,
                   viewModel: ReminderViewModel = viewModel(
                      factory = ReminderViewModelFactory(app)
                  )) {
    val viewState by viewModel.state.collectAsState()

    Surface {
        Column( modifier = screen_modifier) {
            MyTopAppBar(onBackClick = onBackPress,
              onAddClick = { viewModel.addReminder("NewReminder") }
            )
            MyLazyColumn(reminders = viewState.reminders,
                onDeleteClick = { uid-> viewModel.removeReminder(uid = uid)},
                onEditMessage = { content, uid-> viewModel.editReminder(uid = uid, message_content = content)}
            )
        }
    }
}

@Composable
fun MyLazyColumn(reminders: List<Reminder>,
                 onDeleteClick: (Long) -> Unit,
                 onEditMessage: (String, Long)->Unit){

    LazyColumn(contentPadding = message_column_padding,
        modifier = message_column_modifier,
        verticalArrangement = Arrangement.Center) {


        items(items = reminders.sortedBy { message->message.creationTime },
            key = { message->message.creationTime }) {
                message->

            ReminderRow(message_content = message.content!!,
                onDeleteClick = { onDeleteClick(message.uid) },
                onUpdateContent = { onEditMessage(it, message.uid) },
            )
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@Composable
fun MyTopAppBar(onBackClick: () -> Unit, onAddClick: () -> Unit){
    TopAppBar {
        BackIcon(onBackClick = onBackClick)
        Text(text = "Reminder")
        AddIcon(onAddClick = onAddClick)
    }
}

@Composable
fun ReminderRow(message_content: String, onDeleteClick: () -> Unit,
                onUpdateContent: (String) -> Unit) {


    LazyRow {
        item {
            AccountIcon()
        }
        item {
            MyTextField(message_content = message_content,
                onUpdateContent = { onUpdateContent(it) }
            )
        }
        item { DeleteIcon(onDeleteClick = onDeleteClick) }
    }
}

@Composable
fun MyTextField(message_content: String,
                onUpdateContent: (String) -> Unit){
    var stateful_message_content by remember{ mutableStateOf(message_content) }

    OutlinedTextField(
        value = stateful_message_content,
        onValueChange = {stateful_message_content = it},
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Ascii,
            imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                onUpdateContent(stateful_message_content)
            }
        ),
        singleLine = true,
        modifier = Modifier
            .size(width = 200.dp, height = 75.dp)
    )
}