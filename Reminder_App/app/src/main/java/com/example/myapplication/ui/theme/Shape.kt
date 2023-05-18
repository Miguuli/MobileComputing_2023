package com.example.myapplication.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.systemBarsPadding

val Shapes = Shapes(
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

val screen_modifier = Modifier
    .fillMaxSize()
    .systemBarsPadding()
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

