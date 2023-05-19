package com.example.myapplication.ui.reminders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.theme.BackIcon
import com.example.myapplication.theme.screen_modifier
import com.example.myapplication.viewmodels.ReminderViewModel

@Composable
fun ReminderScreen(
    onBackPress: () -> Unit,
    viewModel: ReminderViewModel = hiltViewModel()
) {
    val viewState by viewModel.state.collectAsState()

    Surface {
        Column( modifier = screen_modifier) {
            MyTopAppBar(onBackClick = onBackPress)
            ReminderModifyScaffold(reminders = viewState.reminders,
                onAddReminder = { time, content, ->
                    viewModel.addReminder(
                        reminderTime = time,
                        message_content = content
                    )},
                onDeleteClick = { uid-> /*viewModel.removeReminder(uid = uid) */},
                onEditReminder = {time, content, uid->
                    /*viewModel.editReminder(time = time,
                     message_content = content, uid = uid)*/
                }
            )
        }
    }
}

@Composable
fun MyTopAppBar(onBackClick: () -> Unit){
    TopAppBar {
        BackIcon(onBackClick = onBackClick)
        Text(text = "Reminder")
        Spacer(modifier = Modifier.width(130.dp))
    }
}