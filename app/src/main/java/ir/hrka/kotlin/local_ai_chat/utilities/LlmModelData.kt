package ir.hrka.kotlin.local_ai_chat.utilities

data class LlmModelData (
    val modelId: Int,
    val modelName: String,
    val fileUrl: String,
    val fileSize: Long,
    val backend: String,
    val quantizationScheme: String,
    val contextLength: String,
    val prefill: String,
    val decode: String,
    val timeToFirstToken: Float,
    val cpuMemory: Int,
    val gpuMemory: Int
)