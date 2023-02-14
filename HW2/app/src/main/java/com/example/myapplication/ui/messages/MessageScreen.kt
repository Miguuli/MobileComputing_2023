package com.example.myapplication.ui.messages

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.theme.*
import com.example.myapplication.viewmodels.MessageViewModel
import com.example.myapplication.viewmodels.MessageViewModelFactory

@Composable
fun MessageScreen(app: Application,
                  onBackPress: () -> Unit,
                  viewModel: MessageViewModel = viewModel(
                      factory = MessageViewModelFactory(app)
                  )) {
    Surface {
      Column( modifier = screen_modifier) {
          MyTopAppBar(onBackClick = onBackPress,
              onAddClick = { viewModel.addMessage("NewMessage") }
          )
          LazyColumn(contentPadding = message_column_padding,
              modifier = message_column_modifier) {

              val iterator = viewModel.messages.listIterator()

              iterator.withIndex().forEach {
                  item{
                      val index = it.index
                      val content = it.value

                      MessageRow(message_content = content,
                          onDeleteClick = { viewModel.removeMessage(index = it.index) },
                          onUpdate = { viewModel.updateEnable(it) },
                          onUpdateContent = { viewModel.updateContent(message_content = content, index = index)},
                          flag = viewModel.enabled
                      )
                      Spacer(modifier = Modifier.height(5.dp))
                  }
              }
          }
      }
  }
}

@Composable
fun MyTopAppBar(onBackClick: () -> Unit, onAddClick: () -> Unit){
    TopAppBar {
        BackIcon(onBackClick = onBackClick)
        Text(text = "Messages")
        AddIcon(onAddClick = onAddClick)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MessageRow(message_content: String, onDeleteClick: () -> Unit,
               onUpdate: (Boolean) -> Unit, flag: Boolean,
               onUpdateContent: (String) -> Unit) {

    val (focusRequester) = FocusRequester.createRefs()

    Row {
        AccountIcon()
        MyTextField(flag = flag, message_content = message_content,
            onUpdate = onUpdate,
            onUpdateContent = onUpdateContent, focusRequester = focusRequester)
        DeleteIcon(onDeleteClick = onDeleteClick)
        EditIcon(onClick = {
            focusRequester.requestFocus()
            onUpdate(true)
        })
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MyTextField(flag: Boolean, message_content: String,
                onUpdate: (Boolean) -> Unit, onUpdateContent: (String) -> Unit,
                focusRequester: FocusRequester){
    var stateful_message_content by remember{ mutableStateOf(message_content) }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        enabled = flag,
        value = stateful_message_content,
        onValueChange = {
            stateful_message_content = it
            onUpdateContent(it)},
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Ascii,
            imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                onUpdate(false)
            }),
        singleLine = true,
        modifier = Modifier
            .focusRequester(focusRequester)
            .size(width = 200.dp, height = 75.dp)
    )
}