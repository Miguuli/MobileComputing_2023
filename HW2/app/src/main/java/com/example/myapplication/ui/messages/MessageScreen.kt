package com.example.myapplication.ui.messages

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.entity.Message
import com.example.myapplication.ui.theme.*
import com.example.myapplication.viewmodels.MessageViewModel
import com.example.myapplication.viewmodels.MessageViewModelFactory
import kotlin.random.Random

@Composable
fun MessageScreen(app: Application,
                  onBackPress: () -> Unit,
                  viewModel: MessageViewModel = viewModel(
                      factory = MessageViewModelFactory(app)
                  )) {
    val viewState by viewModel.state.collectAsState()

    val selectedMessage = viewState.selected_message

    Surface {
        Column( modifier = screen_modifier) {
            MyTopAppBar(onBackClick = onBackPress,
              onAddClick = { viewModel.addMessage("NewMessage") }
            )
            MyLazyColumn(messages = viewState.messages,
                onDeleteClick = { uid-> viewModel.removeMessage(uid = uid)},
                onUpdate =  { flag-> viewModel.updateEnable(flag = flag)},
                flag = viewModel.enabled,
                onEditMessage = { content, uid->
                    viewModel.editMessage(message_content = content, uid = uid)}
            )
        }
    }
}

@Composable
fun MyLazyColumn(messages: List<Message>,
                 onDeleteClick: (Long) -> Unit,
                 onUpdate: (Boolean) -> Unit,
                 flag: Boolean,
                 onEditMessage: (String, Long)->Unit){

    LazyColumn(contentPadding = message_column_padding,
        modifier = message_column_modifier,
        verticalArrangement = Arrangement.Center) {


        items(items = messages.sortedBy { message->message.creationTime },
            key = { message->message.creationTime }) {
                message->

            MessageRow(message_content = message.content!!,
                onDeleteClick = { onDeleteClick(message.uid) },
                onUpdate = { onUpdate(it) },
                onUpdateContent = { onEditMessage(it, message.uid) },
                flag = flag
            )
            Spacer(modifier = Modifier.height(5.dp))
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

    LazyRow {
        item {
            AccountIcon()
        }
        item {
            MyTextField(flag = flag, message_content = message_content,
                onUpdate = onUpdate,
                onUpdateContent = {onUpdateContent(it)}, focusRequester = focusRequester)
        }
        item {
            DeleteIcon(onDeleteClick = onDeleteClick)
        }
        item {
            EditIcon(onClick = {
                focusRequester.requestFocus()
                onUpdate(true)
            })
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MyTextField(flag: Boolean, message_content: String,
                onUpdate: (Boolean) -> Unit, onUpdateContent: (String) -> Unit,
                focusRequester: FocusRequester){
    var stateful_message_content by remember{ mutableStateOf("" + message_content) }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        enabled = flag,
        value = stateful_message_content,
        onValueChange = {
            stateful_message_content = it
            onUpdateContent(stateful_message_content) },
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