package ir.hrka.kotlin.local_ai_chat

import android.content.Context
import com.google.common.util.concurrent.ListenableFuture
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.google.mediapipe.tasks.genai.llminference.ProgressListener
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class OnDeviceAiChat @Inject constructor(@ApplicationContext val context: Context) {

    private var generator: ListenableFuture<String>? = null
    private var llmInference: LlmInference? = null


    fun initial(options: LlmInference.LlmInferenceOptions) {
        finalize()
        llmInference = LlmInference.createFromOptions(context, options)
    }

    @Throws(IllegalStateException::class)
    fun generateResponse(prompt: String): Flow<String> {
        return callbackFlow {
            try {
                if (llmInference == null)
                    throw IllegalStateException("LlmInference is not initialized. Call 'initialInference' first.")

                generator = llmInference?.generateResponseAsync(
                    prompt,
                    object : ProgressListener<String> {
                        override fun run(partial: String, done: Boolean) {
                            if (generator?.isCancelled == true) close()
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
        generator?.cancel(true)
        generator = null
    }

    fun finalize() {
        generator?.cancel(true)
        generator = null
        llmInference?.close()
        llmInference = null
    }
}