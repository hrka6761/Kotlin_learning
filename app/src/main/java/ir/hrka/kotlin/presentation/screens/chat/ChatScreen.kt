package ir.hrka.kotlin.presentation.screens.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.chat.ChatState
import ir.hrka.kotlin.core.chat.MessageOwner
import ir.hrka.kotlin.domain.entities.ChatMessage
import ir.hrka.kotlin.presentation.TopBar

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    onTopBarBackPressed: () -> Unit,
    chatMessageList: List<ChatMessage>,
    onUserSendMessage: (text: String) -> Unit,
    onUserStopAnswering: () -> Unit,
    state: ChatState,
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPaddings),
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
                    items(chatMessageList.size) { index ->
                        val chatMessage = chatMessageList[index]
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
                                    text = chatMessage.text,
                                    state = state,
                                )
                        }
                    }
                }

            ChatInputMessage(
                modifier = modifier,
                onUserSendMessage = onUserSendMessage,
                onUserStopAnswering = onUserStopAnswering,
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


@Composable
fun ChatInputMessage(
    modifier: Modifier,
    onUserSendMessage: (text: String) -> Unit,
    onUserStopAnswering: () -> Unit,
    state: ChatState
) {
    var text by remember { mutableStateOf("") }

    TextField(
        maxLines = 3,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
                        onUserStopAnswering()
                },
                enabled = text.isNotEmpty()
            ) {
                Icon(
                    painter = painterResource(
                        if (state == ChatState.UserInput) R.drawable.send else R.drawable.stop
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
    text: String,
    state: ChatState,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
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
                    painter = painterResource(R.drawable.gemini),
                    tint = colorResource(R.color.gemini_icon_color),
                    contentDescription = null
                )

                if (state == ChatState.GenerateResponse && text.isEmpty())
                CircularProgressIndicator(
                    modifier = modifier.padding(2.dp),
                    strokeWidth = 1.dp
                )
            }
        }
        Text(
            modifier = modifier.padding(top = 8.dp),
            text = text,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            overflow = TextOverflow.Clip
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ChatScreenPreview(modifier: Modifier = Modifier) {
    val newResponse by remember { mutableStateOf("") }

    ChatScreen(
        modifier = modifier,
        onTopBarBackPressed = {},
        chatMessageList = mutableListOf<ChatMessage>(
            ChatMessage(
                "Hello",
                MessageOwner.User
            ),
            ChatMessage(
                "Hello how are you?",
                MessageOwner.Gemini
            ),
            ChatMessage(
                "Can you help me?",
                MessageOwner.User
            ),
            ChatMessage(
                "sdihfkldhjlkdsjfjfs skdfl kdfksdksd;l kds;fk sdfk; lsdkksdfsdkfsdkfsdkfksd  sdk;lfksd;kf;ksd;kf   ;lksdfk;skd;   skfdks;dlkf;skdf sd fksd;kf;ksd;fk;sdk sdf ;lsdf sdkf;sdkf;sk d;fk;sd f sdfk;sdk",
                MessageOwner.Gemini
            ),
            ChatMessage(
                "Hello",
                MessageOwner.User
            ),
            ChatMessage(
                newResponse,
                MessageOwner.Gemini
            ),
        ),
        onUserSendMessage = {},
        onUserStopAnswering = {},
        state = ChatState.GenerateResponse,
    )
}