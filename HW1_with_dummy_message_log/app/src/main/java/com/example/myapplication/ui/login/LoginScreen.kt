package com.example.myapplication.ui.login

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.*
import androidx.navigation.NavController
import com.example.myapplication.viewmodels.LoginViewModel
import com.example.myapplication.viewmodels.LoginViewModelFactory
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun LoginScreen(app: Application,
                navController: NavController,
                viewModel: LoginViewModel = viewModel(
                    factory = LoginViewModelFactory(app)
                )
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                onClick = {
                    if(!viewModel.check_for_user() &&
                        username.isNotEmpty() &&
                        password.isNotEmpty()){
                        viewModel.update_username(username)
                        viewModel.update_password(password)
                    }
                    else{
                        val access_granted = viewModel.authorize(username, password)
                        println("access_granted: $access_granted")
                        if(access_granted) navController.navigate("start")
                    } },
                enabled = true,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = "Login")
            }
        }
    }
}