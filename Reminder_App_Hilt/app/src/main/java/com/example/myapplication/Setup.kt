package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.ui.login.LoginScreen
import com.example.myapplication.ui.reminders.ReminderScreen
import com.example.myapplication.ui.start.Start

@Composable
fun SetupMyApplication(
    appState: MyApplicationState = rememberMyApplicationState()
){
    NavHost(
        navController = appState.navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            LoginScreen(navController = appState.navController)
        }
        composable(route = "start") {
            Start(navController = appState.navController)
        }
        composable(route = "reminder") {
            ReminderScreen(
                onBackPress = appState::navigateBack
            )
        }
    }
}