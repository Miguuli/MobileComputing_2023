package com.example.myapplication.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

val message_column_padding = PaddingValues(
    horizontal = 10.dp,
    vertical = 8.dp
)

val message_column_modifier = Modifier
    .fillMaxWidth()
    .padding(10.dp)

@Composable
fun AccountIcon(){
    Icon(
        imageVector = Icons.Default.AccountCircle,
        contentDescription = null,
        modifier = Modifier.size(75.dp)
    )
}

@Composable
fun DeleteIcon(onDeleteClick: () -> Unit){
    IconButton(
        onClick = onDeleteClick
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null
        )
    }
}

@Composable
fun EditIcon(onClick: () -> Unit){
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null
        )
    }

}

@Composable
fun AddIcon(onClick: () -> Unit){
    FloatingActionButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null
        )
    }
}

@Composable
fun NearbyLocations(onShowAll:() -> Unit){
    Button(
        onClick = onShowAll
    ) {
        Text(text = "Explore", fontSize = 10.sp)
    }
}
@Composable
fun cutOutShape(): Shape {
    return MaterialTheme.shapes.small.copy(
        CornerSize(percent = 50)
    )
}


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