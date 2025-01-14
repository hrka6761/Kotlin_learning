package ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities

import kotlinx.coroutines.Job

data class CoroutineData(
    val coroutineName: String,
    val thread: String,
    val job: Job,
    val parentJob: Job?,
    val children: List<Job>
) : ComponentData