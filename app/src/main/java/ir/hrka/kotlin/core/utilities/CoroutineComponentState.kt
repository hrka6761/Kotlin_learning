package ir.hrka.kotlin.core.utilities

sealed class CoroutineComponentState {

    data object Stop : CoroutineComponentState()
    data object Processing : CoroutineComponentState()
    data object Done : CoroutineComponentState()
    data object Cancel : CoroutineComponentState()
    data object Failed : CoroutineComponentState()
}