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
import ir.hrka.kotlin.local_ai_chat.exceptions.ManuallyGenerationCancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ChatViewModel @Inject constructor(
    @Named("Default") private val default: CoroutineDispatcher,
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
    private var _isChatInitialized: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isChatInitialized: StateFlow<Boolean> = _isChatInitialized
    private var chatCount = 0


    fun setChatState(state: ChatState) {
        _chatState.value = state
    }

    fun initialChat() {
        if (!_isChatInitialized.value)
            viewModelScope.launch(default) {
                onDeviceAiChat.initial(options = currentLlmOption.value)
                _isChatInitialized.value = true
            }
    }

    fun ask(userInput: String) {
        setChatState(ChatState.GenerateResponse)

        val list = mutableStateListOf<ChatMessage>()

        list.addAll(_chatMessages.value)
        list.add(
            ChatMessage(
                id = chatCount,
                text = userInput,
                owner = MessageOwner.User
            )
        )
        chatCount++
        list.add(
            ChatMessage(
                id = chatCount,
                text = aiResponse.value,
                owner = MessageOwner.Gemini
            )
        )
        chatCount++
        _chatMessages.value = list

        generateResponse(userInput)
    }

    fun stopAnswering() {
        onDeviceAiChat.stopGenerate()
        setChatState(ChatState.UserInput)
    }


    private fun generateResponse(userInput: String) {
        val response = StringBuilder("")

        viewModelScope.launch(Dispatchers.Default) {
            onDeviceAiChat
                .generateResponse(userInput)
                .onEach {
                    setChatState(ChatState.ProvideResponse)
                    response.append(it)
                    _chatMessages.value = _chatMessages.value.map { chatMessage ->
                        if (chatMessage.id == _chatMessages.value.lastIndex)
                            chatMessage.copy(text = response.toString())
                        else
                            chatMessage
                    }
                }
                .onCompletion {
                    setChatState(ChatState.UserInput)
                }
                .catch {
                    if (it is ManuallyGenerationCancellationException) {
                        _chatMessages.value = _chatMessages.value.map { chatMessage ->
                            if (chatMessage.id == _chatMessages.value.lastIndex) {
                                response.append("   ...")
                                chatMessage.copy(text = response.toString())
                            } else
                                chatMessage
                        }
                    }
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
            .setMaxTokens(4096)
            .setMaxTopK(40)
            .setPreferredBackend(Backend.CPU)
            .build()
    }
}