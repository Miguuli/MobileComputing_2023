package com.example.myapplication.ui.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.myapplication.domain.Message
import com.example.myapplication.ui.theme.Purple200
import com.example.myapplication.ui.theme.Shapes
import com.example.myapplication.ui.theme.Teal200
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun MessageScreen(onBackPress: () -> Unit) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar {
                IconButton(
                    onClick = onBackPress
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
                Text(text = "Messages")
            }
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                ,modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)){
                val message = Message()
                message.message_list.addAll(
                    listOf("Hello1", "Hello2", "Hello3", "Hello4", "Hello5",
                            "Hello6", "Hello7", "Hello8", "Hello9", "Hello10"))
                items(message.message_list){ message_content->
                    Row{
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(75.dp)
                        )
                        Box(contentAlignment= Alignment.CenterStart,
                            modifier = Modifier
                                .size(width = 340.dp, height = 75.dp)
                                .clip(shape = RectangleShape)
                                .fillMaxWidth()
                                .border(2.dp, color = Purple200, shape = Shapes.medium)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        listOf(
                                            MaterialTheme.colors.background,
                                            MaterialTheme.colors.background
                                        )
                                    )
                                )
                        ){
                            Text(text = message_content, modifier = Modifier.padding(4.dp))
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
    }
}