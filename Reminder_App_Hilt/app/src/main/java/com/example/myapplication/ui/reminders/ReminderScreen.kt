package com.example.myapplication.ui.reminders

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.theme.BackIcon
import com.example.myapplication.theme.screen_modifier
import com.example.myapplication.viewmodels.ReminderViewModel
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RequestPermission(onPermissionClick: ()-> Unit){
    //removed button, using coroutine instead
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            onPermissionClick()
        } else {
            // Permission Denied: Do something
            // Show a snackbar or dialog to inform the user of the permission status
        }
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) -> {
                // Some works that require permission
                onPermissionClick()
            }
            else -> {
                // Asking for permission
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
@Composable
fun ReminderScreen(
    onBackPress: () -> Unit,
    viewModel: ReminderViewModel = hiltViewModel()
) {
    val viewState by viewModel.state.collectAsState()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        RequestPermission(onPermissionClick = {  })

    Surface {
        Column( modifier = screen_modifier) {
            MyTopAppBar(onBackClick = onBackPress)
            ReminderModifyScaffold(reminders = viewState.reminders,
                onAddReminder = { time, content, ->
                    viewModel.addReminder(
                        reminderTime = time,
                        message_content = content
                    )},
                onDeleteClick = { uid-> viewModel.removeReminder(uid = uid) },
                onEditReminder = {time, content, uid->
                    viewModel.editReminder(time = time,
                     message_content = content, uid = uid)
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