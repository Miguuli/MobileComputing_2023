package com.example.myapplication.ui.theme

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)
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
fun AddIcon(onAddClick: () -> Unit){
    IconButton(
        onClick = onAddClick
    ) {
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