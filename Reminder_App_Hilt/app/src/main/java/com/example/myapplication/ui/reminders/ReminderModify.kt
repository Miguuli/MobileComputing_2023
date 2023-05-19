package com.example.myapplication.ui.reminders

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.core.model.entity.Reminder
import com.example.myapplication.theme.AddIcon
import com.example.myapplication.theme.cutOutShape
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReminderModifyScaffold(
    reminders: List<Reminder>,
    onAddReminder: (String, String) -> Unit,
    onDeleteClick: (Long) -> Unit,
    onEditReminder: (String, String, Long) -> Unit
){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { ReminderAddDrawerContent { time, content ->
            onAddReminder(time, content) } },
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
        ReminderContentColumn(reminders = reminders,
            onDeleteClick = { uid-> onDeleteClick(uid) },
            onEditMessage = { time, content, uid-> onEditReminder(time, content, uid) }
        )
    }
}

/**
 * Credit for dropdownmenu: https://www.geeksforgeeks.org/drop-down-menu-in-android-using-jetpack-compose/
 */
@Composable
fun ReminderAddDrawerContent(onDone: (String, String) -> Unit){
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

/**
 * Credit: https://www.codexpedia.com/android/android-jetpack-compose-show-alertdialog-on-button-click/
 */
@Composable
fun ReminderEditDialog(msg : String,
                       showDialog: Boolean,
                       onDismiss: () -> Unit,
                       onEditMessage: (String, String) -> Unit) {

    if (showDialog) {
        AlertDialog(
            modifier = Modifier.size(width = 250.dp, height = 240.dp),
            title = {
                Text(msg)
            },
            text = {
                ReminderEditDialogContent(onDone =  { time, content-> onEditMessage(time, content)})
            },
            onDismissRequest = onDismiss,
            buttons = {
                TextButton(onClick = onDismiss ) {
                    Text("Dismiss")
                }
            },
        )
    }
}

@Composable
fun ReminderEditDialogContent(onDone: (String, String)-> Unit) {
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
                    .size(width = 150.dp, height = 50.dp)
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
                    .size(width = 150.dp, height = 50.dp)
            )
        }
    }
}

