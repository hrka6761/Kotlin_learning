package ir.hrka.kotlin.local_ai_chat

import android.content.Context
import com.google.common.util.concurrent.ListenableFuture
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.google.mediapipe.tasks.genai.llminference.LlmInferenceSession
import com.google.mediapipe.tasks.genai.llminference.ProgressListener
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.hrka.kotlin.local_ai_chat.exceptions.ManuallyGenerationCancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class OnDeviceAiChat @Inject constructor(@ApplicationContext val context: Context) {

    private var generator: ListenableFuture<String>? = null
    private var llmInferenceSession: LlmInferenceSession? = null
    private var llmInference: LlmInference? = null
    private var isInitialized: Boolean = false


    fun initial(
        options: LlmInference.LlmInferenceOptions,
        sessionOptions: LlmInferenceSession.LlmInferenceSessionOptions = getDefaultSessionOptions()
    ) {
        finalize()
        llmInference = LlmInference.createFromOptions(context, options)
        llmInferenceSession = LlmInferenceSession.createFromOptions(llmInference, sessionOptions)
        isInitialized = true
    }

    @Throws(IllegalStateException::class)
    fun generateResponse(prompt: String): Flow<String> {
        return callbackFlow {
            try {
                if (!isInitialized)
                    throw IllegalStateException("LlmInference is not initialized. Call 'initialInference' first.")

                llmInferenceSession?.addQueryChunk(prompt)
                generator = llmInferenceSession?.generateResponseAsync(
                    object : ProgressListener<String> {
                        override fun run(partial: String, done: Boolean) {
                            if (generator?.isCancelled != false) {
                                close(ManuallyGenerationCancellationException())
                                return
                            }
                            trySend(partial)
                            if (done) close()
                        }
                    }
                )
            } catch (e: Exception) {
                close(e)
            }

            awaitClose { generator = null }
        }
    }

    fun stopGenerate() {
        llmInferenceSession?.cancelGenerateResponseAsync()
        generator?.cancel(true)
        generator = null
    }

    fun finalize() {
        stopGenerate()
        llmInference?.close()
        llmInference = null
    }


    private fun getDefaultSessionOptions() =
        LlmInferenceSession.LlmInferenceSessionOptions.builder().build()
}