package com.example.myapplication.ui.messages

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextInputService
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.getColumnIndex
import com.example.myapplication.ui.theme.Purple200
import com.example.myapplication.ui.theme.Shapes
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
              val iterator = viewModel.messages.listIterator()

              iterator.withIndex().forEach {
                  item{
                      MessageRow(message_content = it.value,
                          onDeleteClick = { viewModel.removeMessage(index = it.index) })
                      Spacer(modifier = Modifier.height(5.dp))
                  }
              }
          }
      }
  }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MessageRow(message_content: String, onDeleteClick: () ->Unit) {
    var stateful_message_content by remember{ mutableStateOf(message_content) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val (focusRequester) = FocusRequester.createRefs()
    var enabled by remember { mutableStateOf(false)}

    Row {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(75.dp)
        )
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .size(width = 160.dp, height = 75.dp)
                .clip(shape = RectangleShape)
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
            TextField(
                enabled = enabled,
                value = stateful_message_content,
                onValueChange = { stateful_message_content = it},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii,
                    imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        enabled = false
                    }),
                singleLine = true,
                modifier = Modifier.focusRequester(focusRequester)
            )
        }
        IconButton(
            onClick = onDeleteClick
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null
            )
        }
        IconButton(
            onClick = {
                focusRequester.requestFocus()
                enabled = true
            }
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
        }
    }
}