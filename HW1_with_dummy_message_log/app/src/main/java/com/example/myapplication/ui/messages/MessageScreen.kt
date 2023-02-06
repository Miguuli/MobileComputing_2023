package com.example.myapplication.ui.messages

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.domain.Message
import com.example.myapplication.ui.theme.Purple200
import com.example.myapplication.ui.theme.Shapes
import com.example.myapplication.ui.theme.Teal200
import com.example.myapplication.viewmodels.LoginViewModel
import com.example.myapplication.viewmodels.LoginViewModelFactory
import com.example.myapplication.viewmodels.MessageViewModel
import com.example.myapplication.viewmodels.MessageViewModelFactory
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun MessageScreen(app: Application,
                  onBackPress: () -> Unit,
                  viewModel: MessageViewModel = viewModel(
                      factory = MessageViewModelFactory(app)
                  )) {
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
              IconButton(
                  onClick = {
                      viewModel.addMessage("NewMessage")
                  }
              ) {
                  Icon(
                      imageVector = Icons.Default.Add,
                      contentDescription = null
                  )
              }
          }
          LazyColumn(
              contentPadding = PaddingValues(
                  horizontal = 10.dp,
                  vertical = 8.dp
              ), modifier = Modifier
                  .fillMaxWidth()
                  .padding(10.dp)
          ) {

              items(viewModel.messages) { message_content ->
                  Row {
                      Icon(
                          imageVector = Icons.Default.AccountCircle,
                          contentDescription = null,
                          modifier = Modifier.size(75.dp)
                      )
                      Box(
                          contentAlignment = Alignment.CenterStart,
                          modifier = Modifier
                              .size(width = 340.dp, height = 75.dp)
                              .clip(shape = RectangleShape)
                              .fillMaxWidth()
                              .border(
                                  2.dp,
                                  color = Purple200,
                                  shape = Shapes.medium
                              )
                              .background(
                                  brush = Brush.horizontalGradient(
                                      listOf(
                                          MaterialTheme.colors.background,
                                          MaterialTheme.colors.background
                                      )
                                  )
                              )
                      ) {
                          Text(
                              text = message_content,
                              modifier = Modifier.padding(4.dp)
                          )
                      }
                      Spacer(modifier = Modifier.height(5.dp))
                  }
              }
          }
      }
  }
}