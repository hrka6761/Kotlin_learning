package ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities

import kotlinx.coroutines.Job

data class ScopeData(
    val scopeName: String,
    val coroutineName: String?,
    val thread: String,
    val job: Job,
    val parentJob: Job?,
    val children: List<Job>
) : ComponentData
