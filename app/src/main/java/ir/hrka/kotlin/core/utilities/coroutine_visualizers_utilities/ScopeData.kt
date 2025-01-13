package ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities

import kotlinx.coroutines.Job

data class ScopeData(
    val scopeName: String,
    val coroutineName: String,
    val thread: String,
    val job: String,
    val parentJob: String,
    val children: List<Job>
) : ComponentData
