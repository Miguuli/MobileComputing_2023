package com.example.myapplication.ui.reminders

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import com.example.myapplication.ui.theme.*
import com.example.myapplication.viewmodels.ReminderViewModel
import com.example.myapplication.viewmodels.ReminderViewModelFactory

@Composable
fun ReminderScreen(
    app: Application,
    navController: NavHostController,
    onBackPress: () -> Unit,
    viewModel: ReminderViewModel = viewModel(
        factory = ReminderViewModelFactory(app)
    )
) {
    val viewState by viewModel.state.collectAsState()
    class Test: Navigator.Extras{

    }
    Surface {
        Column( modifier = screen_modifier) {
            MyTopAppBar(onBackClick = onBackPress, enabled = viewModel.enabled)
            ReminderModifyScaffold(reminders = viewState.reminders,
                onMapNavigate = {locationX, locationY->navController.navigate("map")},
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
fun MyTopAppBar(onBackClick: () -> Unit, enabled: Boolean){
    TopAppBar {
        BackIcon(onBackClick = onBackClick)
        Text(text = "Reminder")
        Spacer(modifier = Modifier.width(130.dp))
        //ShowAllButton(enabled = enabled, onShowAll = {onShowAll(it)})
    }
}