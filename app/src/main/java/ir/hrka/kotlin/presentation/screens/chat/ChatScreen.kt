package ir.hrka.kotlin.presentation.screens.chat

import android.widget.TextView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon
import ir.hrka.kotlin.R
import ir.hrka.kotlin.local_ai_chat.utilities.ChatState
import ir.hrka.kotlin.local_ai_chat.utilities.MessageOwner
import ir.hrka.kotlin.domain.entities.ChatMessage
import ir.hrka.kotlin.presentation.MainActivity
import ir.hrka.kotlin.presentation.TopBar

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    activity: MainActivity,
    onTopBarBackPressed: () -> Unit,
    chatMessageList: List<ChatMessage>,
    onUserAsk: (text: String) -> Unit,
    onStopAnswering: () -> Unit,
    state: ChatState,
    isChatInitialized: Boolean
) {
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                modifier = modifier,
                title = stringResource(R.string.chat_app_bar_title),
                navigationClick = { onTopBarBackPressed() }
            )
        },
        snackbarHost = {
            SnackbarHost(
                modifier = modifier.fillMaxWidth(),
                hostState = snackBarHostState
            )
        }
    ) { innerPaddings ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPaddings),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                val listState = rememberLazyListState()

                LaunchedEffect(chatMessageList.size) {
                    if (chatMessageList.isNotEmpty())
                        listState.animateScrollToItem(chatMessageList.lastIndex)
                }

                if (chatMessageList.isEmpty())
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = stringResource(R.string.chat_place_holder),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                else
                    LazyColumn(
                        modifier = modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(bottom = 8.dp),
                        state = listState,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        items(
                            items = chatMessageList,
                            key = { chatMessage -> chatMessage.id }
                        ) { chatMessage ->
                            val owner = chatMessage.owner

                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                if (owner == MessageOwner.User)
                                    UserRequest(
                                        modifier = modifier,
                                        text = chatMessage.text
                                    )
                                else
                                    GeminiResponse(
                                        modifier = modifier,
                                        activity = activity,
                                        text = chatMessage.text,
                                        state = state,
                                        isLastResponse = chatMessage.id == chatMessageList.lastIndex
                                    )
                            }
                        }
                    }

                ChatInputMessage(
                    modifier = modifier,
                    onUserSendMessage = onUserAsk,
                    onStopAnswering = onStopAnswering,
                    state = state
                )

                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    text = stringResource(R.string.chat_description),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    if (!isChatInitialized)
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = modifier.size(50.dp),
                strokeWidth = 1.dp
            )

            Spacer(
                modifier = modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            Text(
                text = stringResource(R.string.chat_initializing_msg),
                fontWeight = FontWeight.Bold
            )
        }
}


@Composable
fun ChatInputMessage(
    modifier: Modifier,
    onUserSendMessage: (text: String) -> Unit,
    onStopAnswering: () -> Unit,
    state: ChatState
) {
    var text by remember { mutableStateOf("") }

    TextField(
        maxLines = 3,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 6.dp, end = 6.dp, top = 16.dp),
        value = text,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        placeholder = { Text(stringResource(R.string.chat_input_place_holder_txt)) },
        onValueChange = { text = it },
        trailingIcon = {
            IconButton(
                modifier = modifier.padding(end = 4.dp),
                onClick = {
                    if (state == ChatState.UserInput) {
                        onUserSendMessage(text)
                        text = ""
                    } else
                        onStopAnswering()
                },
                enabled = if (state == ChatState.UserInput) text.isNotEmpty() else true
            ) {
                Icon(
                    painter = painterResource(
                        if (state == ChatState.UserInput)
                            R.drawable.send
                        else
                            R.drawable.stop
                    ),
                    contentDescription = null,
                )
            }
        },
        shape = RoundedCornerShape(30.dp)
    )
}

@Composable
fun UserRequest(
    modifier: Modifier,
    text: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 36.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.End
    ) {
        Card(
            modifier = Modifier.padding(24.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                overflow = TextOverflow.Clip,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun GeminiResponse(
    modifier: Modifier,
    activity: MainActivity,
    text: String,
    state: ChatState,
    isLastResponse: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.safe_mind),
                    tint = colorResource(R.color.gemini_icon_color),
                    contentDescription = null
                )

                if (state == ChatState.GenerateResponse && isLastResponse)
                    CircularProgressIndicator(
                        strokeWidth = 1.dp
                    )
            }
        }

        MarkdownText(
            activity = activity,
            markdown = text,
            modifier = modifier
        )
    }
}

@Composable
fun MarkdownText(activity: MainActivity, markdown: String, modifier: Modifier = Modifier) {
    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) Color.White else Color.Black

    val markwon = remember {
        Markwon.create(activity)
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            TextView(ctx).apply {
                setTextIsSelectable(true)
                textSize = 16f
                setTextColor(textColor.toArgb())
            }
        },
        update = { textView ->
            textView.setTextColor(textColor.toArgb())
            markwon.setMarkdown(textView, markdown)
        }
    )
}


@Preview(showBackground = true)
@Composable
fun ChatScreenPreview(modifier: Modifier = Modifier) {
    val newResponse by remember { mutableStateOf("") }

    ChatScreen(
        modifier = modifier,
        activity = MainActivity(),
        onTopBarBackPressed = {},
        chatMessageList = mutableListOf<ChatMessage>(
            ChatMessage(
                1,
                "Hello",
                MessageOwner.User
            ),
            ChatMessage(
                2,
                "Hello how are you?",
                MessageOwner.Gemini
            ),
            ChatMessage(
                3,
                "Can you help me?",
                MessageOwner.User
            ),
            ChatMessage(
                4,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                MessageOwner.Gemini
            ),
            ChatMessage(
                5,
                "Hello",
                MessageOwner.User
            ),
            ChatMessage(
                6,
                newResponse,
                MessageOwner.Gemini
            ),
        ),
        onUserAsk = {},
        onStopAnswering = {},
        state = ChatState.UserInput,
        isChatInitialized = false
    )
}