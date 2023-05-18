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

    Surface {
        Column( modifier = screen_modifier) {
            MyTopAppBar(onBackClick = onBackPress, onNearbyLocations = {
                val content = "reference_point"
                val locationX = 65.012818
                val locationY = 25.469826
                navController.navigate("map/$content/$locationX/$locationY")
            })
            ReminderModifyScaffold(reminders = viewState.reminders,
                onMapNavigate = {content, locationX, locationY->
                    navController.navigate("map/$content/$locationX/$locationY")},
                onAddReminder = {location,  time, content, ->
                    viewModel.addReminder(
                        location = location,
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
fun MyTopAppBar(onBackClick: () -> Unit, onNearbyLocations: () -> Unit){
    TopAppBar {
        BackIcon(onBackClick = onBackClick)
        Text(text = "Reminder")
        Spacer(modifier = Modifier.width(130.dp))
        NearbyLocations(onShowAll = onNearbyLocations)
    }
}