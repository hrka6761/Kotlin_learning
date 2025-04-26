package ir.hrka.kotlin.presentation.ui.screens.visualizers.regular_coroutine_scope_function

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.Constants.TAG
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Stop
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Processing
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Done
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.CoroutineData
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ScopeData
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.TaskData
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ThreadData
import ir.hrka.kotlin.presentation.GlobalData
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CoroutineScopeFunctionViewModel @Inject constructor(
    private val globalData: GlobalData
) : ViewModel() {

    private val _executionState: MutableStateFlow<ExecutionState> =
        MutableStateFlow(ExecutionState.Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _mainThreadState: MutableLiveData<ComponentState<ThreadData>> =
        MutableLiveData(Stop())
    val mainThreadState: LiveData<ComponentState<ThreadData>> = _mainThreadState
    private val _scopeState: MutableLiveData<ComponentState<ScopeData>> =
        MutableLiveData(Stop())
    val scopeState: LiveData<ComponentState<ScopeData>> = _scopeState
    private val _coroutine1State: MutableLiveData<ComponentState<CoroutineData>> =
        MutableLiveData(Stop())
    val coroutine1State: LiveData<ComponentState<CoroutineData>> = _coroutine1State
    private val _coroutine2State: MutableLiveData<ComponentState<CoroutineData>> =
        MutableLiveData(Stop())
    val coroutine2State: LiveData<ComponentState<CoroutineData>> = _coroutine2State
    private val _coroutine3State: MutableLiveData<ComponentState<CoroutineData>> =
        MutableLiveData(Stop())
    val coroutine3State: LiveData<ComponentState<CoroutineData>> = _coroutine3State
    private val _task1State: MutableLiveData<ComponentState<TaskData>> =
        MutableLiveData(Stop())
    val task1State: LiveData<ComponentState<TaskData>> = _task1State
    private val _task2State: MutableLiveData<ComponentState<TaskData>> =
        MutableLiveData(Stop())
    val task2State: LiveData<ComponentState<TaskData>> = _task2State
    private val _task3State: MutableLiveData<ComponentState<TaskData>> =
        MutableLiveData(Stop())
    val task3State: LiveData<ComponentState<TaskData>> = _task3State
    private val mainThreadData = ThreadData("2", "main")


    fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    fun runAllTasks() {
//        I had to use another thread to visualize and manually named the thread name and ID here.
        Thread {
            _mainThreadState.postValue(Processing(mainThreadData))
            val threads = Dispatchers.Main
            val scope = CoroutineScope(threads)
            val scopeData = ScopeData(
                scopeName = "CoroutineScope function",
                coroutineName = if (scope.coroutineContext[CoroutineName] != null) scope.coroutineContext[CoroutineName].toString() else null,
                thread = "main",
                job = scope.coroutineContext.job,
                parentJob = scope.coroutineContext.job.parent,
                children = mutableListOf()
            )
            _scopeState.postValue(Processing(scopeData))

            scope.launch {
                val coroutine1Data = CoroutineData(
                    coroutineName = Thread.currentThread().name.split(" ").last(),
                    thread = Thread.currentThread().name.split(" ").first(),
                    job = this.coroutineContext.job,
                    parentJob = this.coroutineContext.job.parent,
                    children = mutableListOf()
                )
                _coroutine1State.postValue(Processing(coroutine1Data))
                task1()
                _coroutine1State.postValue(Done(coroutine1Data))
            }

            scope.launch {
                val coroutine2Data = CoroutineData(
                    coroutineName = Thread.currentThread().name.split(" ").last(),
                    thread = Thread.currentThread().name.split(" ").first(),
                    job = this.coroutineContext.job,
                    parentJob = this.coroutineContext.job.parent,
                    children = mutableListOf()
                )
                _coroutine2State.postValue(Processing(coroutine2Data))
                task2()
                _coroutine2State.postValue(Done(coroutine2Data))
                _scopeState.postValue(Done(scopeData))
                _mainThreadState.postValue(Done(mainThreadData))
                setExecutionState(ExecutionState.Stop)
            }

            scope.launch {
                val coroutine3Data = CoroutineData(
                    coroutineName = Thread.currentThread().name.split(" ").last(),
                    thread = Thread.currentThread().name.split(" ").first(),
                    job = this.coroutineContext.job,
                    parentJob = this.coroutineContext.job.parent,
                    children = mutableListOf()
                )
                (scopeData.children as MutableList<Job>).addAll(scope.coroutineContext.job.children)
                _scopeState.postValue(Processing(scopeData))
                _coroutine3State.postValue(Processing(coroutine3Data))
                task3()
                _coroutine3State.postValue(Done(coroutine3Data))
            }
        }.start()
    }

    fun restartVisualizer() {
        setExecutionState(ExecutionState.Start)
        _coroutine1State.value = Stop()
        _coroutine2State.value = Stop()
        _coroutine3State.value = Stop()
        _task1State.value = Stop()
        _task2State.value = Stop()
        _task3State.value = Stop()
        runAllTasks()
    }

    private suspend fun task1() {
        val taskData = TaskData(
            "First Task",
            "3000"
        )
        _task1State.postValue(Processing(taskData))
        delay(1_000)
        _task1State.postValue(Done(taskData))
    }

    private suspend fun task2() {
        val taskData = TaskData(
            "Second Task",
            "4000"
        )
        _task2State.postValue(Processing(taskData))
        delay(4_000)
        _task2State.postValue(Done(taskData))
    }

    private suspend fun task3() {
        val taskData = TaskData(
            "Third Task",
            "2000"
        )
        _task3State.postValue(Processing(taskData))
        delay(2_000)
        _task3State.postValue(Done(taskData))
    }


    fun main() {
        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch {
            task1()
        }

        scope.launch {
            task2()
        }

        scope.launch {
            task3()
        }
    }
}