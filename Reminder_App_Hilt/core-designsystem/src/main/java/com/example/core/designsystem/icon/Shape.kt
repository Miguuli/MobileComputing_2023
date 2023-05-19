package com.example.core.designsystem.icon

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

val screen_modifier = Modifier
    .fillMaxSize()
    .systemBarsPadding()

@Composable
fun BackIcon(onBackClick: () -> Unit){
    IconButton(
        onClick = onBackClick
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null
        )
    }
}

@Composable
fun SettingsIcon(onSettingsClick: () -> Unit){
    IconButton(onClick =  onSettingsClick) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null
        )
    }
}