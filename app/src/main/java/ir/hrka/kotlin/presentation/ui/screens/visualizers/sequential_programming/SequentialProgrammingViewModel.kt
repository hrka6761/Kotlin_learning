package ir.hrka.kotlin.presentation.ui.screens.visualizers.sequential_programming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.CoroutineComponentState
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.CoroutineComponentState.Stop
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.CoroutineComponentState.Processing
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.CoroutineComponentState.Done
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.TaskData
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ThreadData
import ir.hrka.kotlin.presentation.GlobalData
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SequentialProgrammingViewModel @Inject constructor(
    private val globalData: GlobalData
) : ViewModel() {

    private val _executionState: MutableStateFlow<ExecutionState> =
        MutableStateFlow(ExecutionState.Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _mainThreadState: MutableLiveData<CoroutineComponentState<ThreadData>> =
        MutableLiveData(Stop())
    val mainThreadState: LiveData<CoroutineComponentState<ThreadData>> = _mainThreadState
    private val _task1State: MutableLiveData<CoroutineComponentState<TaskData>> =
        MutableLiveData(Stop())
    val task1State: LiveData<CoroutineComponentState<TaskData>> = _task1State
    private val _task2State: MutableLiveData<CoroutineComponentState<TaskData>> =
        MutableLiveData(Stop())
    val task2State: LiveData<CoroutineComponentState<TaskData>> = _task2State
    private val _task3State: MutableLiveData<CoroutineComponentState<TaskData>> =
        MutableLiveData(Stop())
    val task3State: LiveData<CoroutineComponentState<TaskData>> = _task3State


    fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    fun runAllTasks() {
//        I had to use another thread to visualize and manually named the thread name and ID here.
        Thread {
            val threadData = ThreadData(
                "2",
                "main"
            )
            _mainThreadState.postValue(Processing(threadData))
            task1()
            task2()
            task3()
            _mainThreadState.postValue(Done(threadData))
            setExecutionState(ExecutionState.Stop)
        }.start()
    }

    fun restartVisualizer() {
        setExecutionState(ExecutionState.Start)
        _task1State.value = Stop()
        _task2State.value = Stop()
        _task3State.value = Stop()
        runAllTasks()
    }

    private fun task1() {
        val taskData = TaskData(
            "First Task",
            "1000"
        )
        _task1State.postValue(Processing(taskData))
        Thread.sleep(1_000)
        _task1State.postValue(Done(taskData))
    }

    private fun task2() {
        val taskData = TaskData(
            "Second Task",
            "4000"
        )
        _task2State.postValue(Processing(taskData))
        Thread.sleep(4_000)
        _task2State.postValue(Done(taskData))
    }

    private fun task3() {
        val taskData = TaskData(
            "Three Task",
            "2000"
        )
        _task3State.postValue(Processing(taskData))
        Thread.sleep(2_000)
        _task3State.postValue(Done(taskData))
    }
}