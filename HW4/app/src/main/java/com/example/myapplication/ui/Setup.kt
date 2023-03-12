package com.example.myapplication.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.MyApplicationState
import com.example.myapplication.rememberMyApplicationState
import com.example.myapplication.ui.login.LoginScreen
import com.example.myapplication.ui.maps.ReminderLocation
import com.example.myapplication.ui.reminders.ReminderScreen
import com.example.myapplication.ui.start.Start

@Composable
fun SetupMyApplication(
    app: Application,
    appState: MyApplicationState = rememberMyApplicationState()
){
    NavHost(
        navController = appState.navController,
        startDestination = "login"
    ) {
        composable(route = "login"){
            LoginScreen(app = app, navController = appState.navController)
        }
        composable(route = "start"){
            Start(navController = appState.navController)
        }
        composable(route = "reminder"){
            ReminderScreen(app = app, navController = appState.navController, onBackPress = appState::navigateBack)
        }
        composable(route = "map"){
            ReminderLocation(app = app, navController = appState.navController)
        }
    }
}