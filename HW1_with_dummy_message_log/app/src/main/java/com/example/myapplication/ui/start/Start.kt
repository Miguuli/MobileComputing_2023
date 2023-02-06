package com.example.myapplication.ui.start
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.insets.systemBarsPadding

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
            onClick = { navController.navigate("message") }
        ) {
            Text("Messages")
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
