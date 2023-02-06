package com.example.myapplication.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.MyApplicationState
import com.example.myapplication.rememberMyApplicationState
import com.example.myapplication.ui.login.LoginScreen
import com.example.myapplication.ui.messages.MessageScreen
import com.example.myapplication.ui.payment.Payment
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
            LoginScreen(app, navController = appState.navController)
        }
        composable(route = "start"){
            Start()
        }
        composable(route = "payment"){
            MessageScreen(onBackPress = appState::navigateBack)
        }
    }
}