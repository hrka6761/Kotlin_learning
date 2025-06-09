package ir.hrka.kotlin.presentation.screens.chat

import android.os.Environment
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mediapipe.tasks.genai.llminference.LlmInference.Backend
import com.google.mediapipe.tasks.genai.llminference.LlmInference.LlmInferenceOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.local_ai_chat.utilities.ChatState
import ir.hrka.kotlin.local_ai_chat.utilities.MessageOwner
import ir.hrka.kotlin.domain.entities.ChatMessage
import ir.hrka.kotlin.local_ai_chat.OnDeviceAiChat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val onDeviceAiChat: OnDeviceAiChat
) : ViewModel() {

    private val _chatMessages: MutableStateFlow<List<ChatMessage>> =
        MutableStateFlow(mutableListOf())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages
    private val _chatState: MutableStateFlow<ChatState> = MutableStateFlow(ChatState.UserInput)
    val chatState: StateFlow<ChatState> = _chatState
    private val aiResponse: MutableStateFlow<String> = MutableStateFlow("")
    private val currentLlmOption: MutableStateFlow<LlmInferenceOptions> =
        MutableStateFlow(getDefaultLlmOptions())


    init {
        initialChat()
    }


    fun setChatState(state: ChatState) {
        _chatState.value = state
    }

    fun addMessage(userInput: String) {
        setChatState(ChatState.GenerateResponse)

        val list = mutableStateListOf<ChatMessage>()

        list.addAll(_chatMessages.value)
        list.add(
            ChatMessage(
                id = list.size,
                text = userInput,
                owner = MessageOwner.User
            )
        )
        list.add(
            ChatMessage(
                id = list.size,
                text = aiResponse.value,
                owner = MessageOwner.Gemini
            )
        )
        _chatMessages.value = list

        generateResponse(userInput)
    }

    fun initialChat() = onDeviceAiChat.initial(currentLlmOption.value)


    private fun generateResponse(userInput: String) {
        val response = StringBuilder("")

        viewModelScope.launch(Dispatchers.Default) {
            onDeviceAiChat
                .generateResponse(userInput)
                .onStart {
                }
                .onEach {
                    setChatState(ChatState.ProvideResponse)
                    response.append(it)
                    _chatMessages.value = _chatMessages.value.map { chatMessage ->
                        if (chatMessage.id == _chatMessages.value.size -1 )
                            chatMessage.copy(text = response.toString())
                        else
                            chatMessage
                    }
                }
                .onCompletion {
                    setChatState(ChatState.UserInput)
                }
                .catch {
                    setChatState(ChatState.UserInput)
                }
                .collect()
        }
    }

    private fun getDefaultLlmOptions(): LlmInferenceOptions {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
            "gemma-3n-E4B-it-int4.task"
        )

        return LlmInferenceOptions.builder()
            .setModelPath(file.absolutePath)
            .setPreferredBackend(Backend.CPU)
            .build()
    }
}