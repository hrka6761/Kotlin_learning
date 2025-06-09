package ir.hrka.kotlin.presentation.screens.chat

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.util.Executors
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.google.mediapipe.tasks.genai.llminference.LlmInference.Backend
import com.google.mediapipe.tasks.genai.llminference.LlmInference.LlmInferenceOptions
import com.google.mediapipe.tasks.genai.llminference.LlmInferenceSession
import com.google.mediapipe.tasks.genai.llminference.LlmInferenceSession.LlmInferenceSessionOptions
import com.google.mediapipe.tasks.genai.llminference.ProgressListener
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.chat.ChatState
import ir.hrka.kotlin.core.chat.MessageOwner
import ir.hrka.kotlin.domain.entities.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    private val _chatMessages: MutableStateFlow<List<ChatMessage>> =
        MutableStateFlow(mutableListOf())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages
    private val _chatState: MutableStateFlow<ChatState> = MutableStateFlow(ChatState.UserInput)
    val chatState: StateFlow<ChatState> = _chatState
    private val _lastResponse: MutableStateFlow<String> = MutableStateFlow("")
    val lastResponse: StateFlow<String> = _lastResponse
    private val file = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
        "gemma-3n-E4B-it-int4.task"
    )
    private val options = LlmInferenceOptions.builder()
        .setModelPath(file.absolutePath)
        .setPreferredBackend(Backend.CPU)
        .build()


    fun addMessage(chatMessage: ChatMessage) {
        val list = mutableListOf<ChatMessage>()
        list.addAll(_chatMessages.value)
        list.add(chatMessage)
        list.add(
            ChatMessage(
                text = _lastResponse.value,
                owner = MessageOwner.Gemini
            )
        )
        _chatMessages.value = list
    }

    fun setChatState(state: ChatState) {
        _chatState.value = state
    }

    fun generateResponse(context: Context, inputText: String) {
        Log.i("TAG", "generateResponse: ")

        viewModelScope.launch(Dispatchers.Default) {
            val llmInference = LlmInference.createFromOptions(context, options)

            val future = llmInference.generateResponseAsync("What is the sealed class in kotlin?",
                object : ProgressListener<String> {
                    override fun run(partial: String, done: Boolean) {

                        if (done) {

                        }
                    }
                }
            )

            /*future.cancel(false)*/
        }


    }
}