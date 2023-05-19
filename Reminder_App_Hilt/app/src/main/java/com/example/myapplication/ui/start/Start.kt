package com.example.myapplication.ui.start
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun Start(navController: NavHostController) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Button(
            enabled = true,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            onClick = { navController.navigate("reminder") }
        ) {
            Text("Reminders")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            enabled = true,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            onClick = { navController.navigate("login") }
        ) {
            Text("Log out")
        }
    }
}
