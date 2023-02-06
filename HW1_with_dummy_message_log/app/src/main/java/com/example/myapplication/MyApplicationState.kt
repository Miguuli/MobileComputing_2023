package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class MyApplicationState(val navController: NavHostController){
    fun navigateBack() {
        navController.popBackStack()
    }
}

@Composable
fun rememberMyApplicationState(
    navController: NavHostController = rememberNavController()
) = remember(navController) {
    MyApplicationState(navController)
}