package com.example.myapplication.ui.reminders

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.entity.Reminder
import com.example.myapplication.ui.theme.*
import com.example.myapplication.viewmodels.ReminderViewModel
import com.example.myapplication.viewmodels.ReminderViewModelFactory

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
                onUpdate =  { flag-> viewModel.updateEnable(flag = flag)},
                flag = viewModel.enabled,
                onEditMessage = { content, uid->
                    viewModel.editReminder(message_content = content, uid = uid)}
            )
        }
    }
}

@Composable
fun MyLazyColumn(reminders: List<Reminder>,
                 onDeleteClick: (Long) -> Unit,
                 onUpdate: (Boolean) -> Unit,
                 flag: Boolean,
                 onEditMessage: (String, Long)->Unit){

    LazyColumn(contentPadding = message_column_padding,
        modifier = message_column_modifier,
        verticalArrangement = Arrangement.Center) {


        items(items = reminders.sortedBy { message->message.creationTime },
            key = { message->message.creationTime }) {
                message->

            ReminderRow(message_content = message.content!!,
                onDeleteClick = { onDeleteClick(message.uid) },
                onUpdate = { onUpdate(it) },
                onUpdateContent = { onEditMessage(it, message.uid) },
                flag = flag
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReminderRow(message_content: String, onDeleteClick: () -> Unit,
                onUpdate: (Boolean) -> Unit, flag: Boolean,
                onUpdateContent: (String) -> Unit) {

    val (focusRequester) = FocusRequester.createRefs()

    LazyRow {
        item {
            AccountIcon()
        }
        item {
            MyTextField(flag = flag, message_content = message_content,
                onUpdate = onUpdate,
                onUpdateContent = {onUpdateContent(it)}, focusRequester = focusRequester)
        }
        item {
            DeleteIcon(onDeleteClick = onDeleteClick)
        }
        item {
            EditIcon(onClick = {
                focusRequester.requestFocus()
                onUpdate(true)
            })
        }
    }
}

@Composable
fun MyTextField(flag: Boolean, message_content: String,
                onUpdate: (Boolean) -> Unit, onUpdateContent: (String) -> Unit,
                focusRequester: FocusRequester){
    var stateful_message_content by remember{ mutableStateOf("" + message_content) }

    OutlinedTextField(
        enabled = flag,
        value = stateful_message_content,
        onValueChange = {
            stateful_message_content = it
            onUpdateContent(stateful_message_content) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Ascii,
            imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                onUpdate(false)
            }),
        singleLine = true,
        modifier = Modifier
            .focusRequester(focusRequester)
            .size(width = 200.dp, height = 75.dp)
    )
}