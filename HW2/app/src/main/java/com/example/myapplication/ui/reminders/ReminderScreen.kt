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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
            MyTopAppBar(onBackClick = onBackPress)
            MyScaffold(reminders = viewState.reminders,
                onAddReminder = { time, content->
                    viewModel.addReminder(
                        reminderTime = time,
                        message_content = content
                    )},
                onDeleteClick = { uid-> viewModel.removeReminder(uid = uid) },
                onEditReminder = {time, content, uid->
                    viewModel.editReminder(time = time, message_content = content, uid = uid)}
            )
        }
    }
}

@Composable
fun MyTopAppBar(onBackClick: () -> Unit){
    TopAppBar {
        BackIcon(onBackClick = onBackClick)
        Text(text = "Reminder")
    }
}

@Composable
fun MyScaffold(
    reminders: List<Reminder>,
    onAddReminder: (String, String) -> Unit,
    onDeleteClick: (Long) -> Unit,
    onEditReminder: (String, String, Long) -> Unit
){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { DrawerContent { time, content -> onAddReminder(time, content) } },
        floatingActionButton = {
            AddIcon(onClick = {
                scope.launch{
                    scaffoldState.drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            })
        },
        isFloatingActionButtonDocked = true,
        bottomBar = { BottomAppBar(cutoutShape = cutOutShape()) { } }
    ) {
        MyLazyColumn(reminders = reminders,
            onDeleteClick = { uid-> onDeleteClick(uid) },
            onEditMessage = { time, content, uid-> onEditReminder(time, content, uid) }
        )
    }
}

/**
 * Credit: https://www.codexpedia.com/android/android-jetpack-compose-show-alertdialog-on-button-click/
 */
@Composable
fun Alert(msg : String,
          showDialog: Boolean,
          onDismiss: () -> Unit,
          onEditMessage: (String, String) -> Unit) {

    if (showDialog) {
        AlertDialog(
            modifier = Modifier.size(width = 200.dp, height = 200.dp),
            title = {
                Text(msg)
            },
            text = {
              EditDrawerContent(onDone =  { time, content-> onEditMessage(time, content)})
            },
            onDismissRequest = onDismiss,
            buttons = {
                TextButton(onClick = onDismiss ) {
                    Text("Ok")
                }
            },
        )
    }
}

@Composable
fun MyLazyColumn(reminders: List<Reminder>,
                 onDeleteClick: (Long) -> Unit,
                 onEditMessage: (String, String, Long)->Unit){

    LazyColumn(contentPadding = message_column_padding,
        modifier = message_column_modifier,
        verticalArrangement = Arrangement.Center) {

        items(items = reminders.sortedBy { message->message.creationTime },
            key = { message->message.creationTime }) {
                message->

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

@Composable
fun ReminderRow(
    message_content: String, reminderTime: String?,
    onDeleteClick: () -> Unit,
    onUpdateContent: (String, String) -> Unit) {

    LazyRow {
        item {
            ReminderContent(
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
                Alert(msg = "Edit reminder",
                    showDialog = showDialog.value,
                    onDismiss = {showDialog.value = false},
                onEditMessage = {time, content-> onUpdateContent(time, content)})
            }
            EditIcon(onClick = {showDialog.value = true}) }
    }
}

@Composable
fun ReminderContent(message_content: String, reminderTime: String?){
    Column {
        Text("Time: $reminderTime")
        Text(message_content)
    }
}

@Composable
fun DrawerContent(onDone: (String, String)-> Unit){
    var stateful_time_content by remember{ mutableStateOf("") }
    var stateful_message_content by remember{ mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Text("Set reminder time")
    OutlinedTextField(
        value = stateful_time_content,
        onValueChange = {stateful_time_content = it},
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        singleLine = true,
        modifier = Modifier
            .size(width = 150.dp, height = 75.dp)
    )

    Text("Set task")
    OutlinedTextField(
        value = stateful_message_content,
        onValueChange = {stateful_message_content = it},
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Ascii,
            imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onDone(stateful_time_content, stateful_message_content)
            }
        ),
        singleLine = true,
        modifier = Modifier
            .size(width = 150.dp, height = 75.dp)
    )
}

@Composable
fun EditDrawerContent(onDone: (String, String)-> Unit) {
    var stateful_time_content by remember { mutableStateOf("") }
    var stateful_message_content by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    LazyColumn{
        item{
            Text("Set reminder time")
            OutlinedTextField(
                value = stateful_time_content,
                onValueChange = { stateful_time_content = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                singleLine = true,
                modifier = Modifier
                    .size(width = 100.dp, height = 50.dp)
            )
        }
        item{
            Text("Set task")
            OutlinedTextField(
                value = stateful_message_content,
                onValueChange = { stateful_message_content = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onDone(stateful_time_content, stateful_message_content)
                    }
                ),
                singleLine = true,
                modifier = Modifier
                    .size(width = 100.dp, height = 50.dp)
            )
        }
    }
}