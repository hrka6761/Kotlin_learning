package ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities

data class CoroutineData(
    val coroutineName: String,
    val threadContext: String,
    val jobContext: String
) : ComponentData