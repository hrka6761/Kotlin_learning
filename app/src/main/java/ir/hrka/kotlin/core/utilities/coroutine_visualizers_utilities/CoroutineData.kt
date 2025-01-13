package ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities

import kotlinx.coroutines.Job

data class CoroutineData(
    val coroutineName: String,
    val thread: String,
    val job: String,
    val parentJob: String,
    val children: List<Job>
) : ComponentData