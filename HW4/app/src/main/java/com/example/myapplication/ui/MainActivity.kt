package com.example.myapplication.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.R
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                // Scaffold with a painter in top used to fix bug
                // causing black / white screen in start
                // in mobilecomputing course sample app
                Scaffold(
                    topBar = {
                        Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = null,
                            Modifier.height(40.dp)
                        )
                    }
                ) {
                    SetupMyApplication(app = requireNotNull(this).application)
                }
            }
        }
    }
}