package ir.hrka.kotlin.presentation.ui.screens.visualizers.multi_threading_programming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Stop
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Processing
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Done
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.TaskData
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ThreadData
import ir.hrka.kotlin.presentation.GlobalData
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MultiThreadProgrammingViewModel @Inject constructor(
    private val globalData: GlobalData
) : ViewModel() {

    private val _executionState: MutableStateFlow<ExecutionState> =
        MutableStateFlow(ExecutionState.Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _mainThreadState: MutableLiveData<ComponentState<ThreadData>> =
        MutableLiveData(Stop())
    val mainThreadState: LiveData<ComponentState<ThreadData>> = _mainThreadState
    private val _thread1State: MutableLiveData<ComponentState<ThreadData>> =
        MutableLiveData(Stop())
    val thread1State: LiveData<ComponentState<ThreadData>> = _thread1State
    private val _thread2State: MutableLiveData<ComponentState<ThreadData>> =
        MutableLiveData(Stop())
    val thread2State: LiveData<ComponentState<ThreadData>> = _thread2State
    private val _task1State: MutableLiveData<ComponentState<TaskData>> =
        MutableLiveData(Stop())
    val task1State: LiveData<ComponentState<TaskData>> = _task1State
    private val _task2State: MutableLiveData<ComponentState<TaskData>> =
        MutableLiveData(Stop())
    val task2State: LiveData<ComponentState<TaskData>> = _task2State
    private val _task3State: MutableLiveData<ComponentState<TaskData>> =
        MutableLiveData(Stop())
    val task3State: LiveData<ComponentState<TaskData>> = _task3State
    private val _task4State: MutableLiveData<ComponentState<TaskData>> =
        MutableLiveData(Stop())
    val task4State: LiveData<ComponentState<TaskData>> = _task4State
    private val _task5State: MutableLiveData<ComponentState<TaskData>> =
        MutableLiveData(Stop())
    val task5State: LiveData<ComponentState<TaskData>> = _task5State
    private val _task6State: MutableLiveData<ComponentState<TaskData>> =
        MutableLiveData(Stop())
    val task6State: LiveData<ComponentState<TaskData>> = _task6State


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
            execution1()
            execution2()
            task6()
            setExecutionState(ExecutionState.Stop)
        }.start()
    }

    fun restartVisualizer() {
        setExecutionState(ExecutionState.Start)
        _thread1State.value = Stop()
        _thread2State.value = Stop()
        _task1State.value = Stop()
        _task2State.value = Stop()
        _task3State.value = Stop()
        _task4State.value = Stop()
        _task5State.value = Stop()
        _task6State.value = Stop()
        runAllTasks()
    }

    private fun execution1() {
        Thread {
            val threadData = ThreadData(
                Thread.currentThread().id.toString(),
                Thread.currentThread().name
            )
            _thread1State.postValue(Processing(threadData))
            task2()
            task3()
            _thread1State.postValue(Done(threadData))
            _mainThreadState.postValue(Done(threadData))
        }.start()
    }

    private fun execution2() {
        Thread {
            val threadData = ThreadData(
                Thread.currentThread().id.toString(),
                Thread.currentThread().name
            )
            _thread2State.postValue(Processing(threadData))
            task4()
            task5()
            _thread2State.postValue(Done(threadData))
        }.start()
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
            "Third Task",
            "2000"
        )
        _task3State.postValue(Processing(taskData))
        Thread.sleep(2_000)
        _task3State.postValue(Done(taskData))
    }

    private fun task4() {
        val taskData = TaskData(
            "Fourth Task",
            "3000"
        )
        _task4State.postValue(Processing(taskData))
        Thread.sleep(3_000)
        _task4State.postValue(Done(taskData))
    }

    private fun task5() {
        val taskData = TaskData(
            "Fifth Task",
            "1000"
        )
        _task5State.postValue(Processing(taskData))
        Thread.sleep(1_000)
        _task5State.postValue(Done(taskData))
    }

    private fun task6() {
        val taskData = TaskData(
            "Sixth Task",
            "2500"
        )
        _task6State.postValue(Processing(taskData))
        Thread.sleep(2_500)
        _task6State.postValue(Done(taskData))
    }
}