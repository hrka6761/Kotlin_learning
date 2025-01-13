package ir.hrka.kotlin.presentation.ui.screens.visualizers.coroutine_scope_function

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
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
    private val _coroutine1State: MutableLiveData<ComponentState<CoroutineData>> =
        MutableLiveData(Stop())
    private val _scopeState: MutableLiveData<ComponentState<ScopeData>> =
        MutableLiveData(Stop())
    val scopeState: LiveData<ComponentState<ScopeData>> = _scopeState
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
    private val _task4State: MutableLiveData<ComponentState<TaskData>> =
        MutableLiveData(Stop())
    val task4State: LiveData<ComponentState<TaskData>> = _task4State
    private val _task5State: MutableLiveData<ComponentState<TaskData>> =
        MutableLiveData(Stop())
    val task5State: LiveData<ComponentState<TaskData>> = _task5State
    private val _task6State: MutableLiveData<ComponentState<TaskData>> =
        MutableLiveData(Stop())
    val task6State: LiveData<ComponentState<TaskData>> = _task6State
    private val mainThreadData = ThreadData("2", "main")

    fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    fun runAllTasks() {
//        I had to use another thread to visualize and manually named the thread name and ID here.
        Thread {
            _mainThreadState.postValue(Processing(mainThreadData))
            task1()

            CoroutineScope(Dispatchers.Main).launch {
                val coroutine1Data = CoroutineData(
                    coroutineName = Thread.currentThread().name.split(" ").last(),
                    thread = Thread.currentThread().name.split(" ").first(),
                    job = this.coroutineContext.job.toString(),
                    parentJob = this.coroutineContext.job.parent.toString(),
                    children = mutableListOf()
                )
                _coroutine1State.postValue(Processing(coroutine1Data))
                task2()

                coroutineScope {
                    val scopeData = ScopeData(
                        scopeName = "coroutineScope function",
                        coroutineName = Thread.currentThread().name.split(" ").last(),
                        thread = Thread.currentThread().name.split(" ").first(),
                        job = this.coroutineContext.job.toString(),
                        parentJob = this.coroutineContext.job.parent.toString(),
                        children = mutableListOf()
                    )
                    _scopeState.postValue(Processing(scopeData))

                    launch {
                        val coroutine3Data = CoroutineData(
                            coroutineName = Thread.currentThread().name.split(" ").last(),
                            thread = Thread.currentThread().name.split(" ").first(),
                            job = this.coroutineContext.job.toString(),
                            parentJob = this.coroutineContext.job.parent.toString(),
                            children = this.coroutineContext.job.children.toList()
                        )
                        _coroutine2State.postValue(Processing(coroutine3Data))
                        task3()
                        _coroutine2State.postValue(Done(coroutine3Data))
                    }
                    launch {
                        val coroutine4Data = CoroutineData(
                            coroutineName = Thread.currentThread().name.split(" ").last(),
                            thread = Thread.currentThread().name.split(" ").first(),
                            job = this.coroutineContext.job.toString(),
                            parentJob = this.coroutineContext.job.parent.toString(),
                            children = this.coroutineContext.job.children.toList()
                        )
                        _coroutine3State.postValue(Processing(coroutine4Data))
                        task4()
                        _coroutine3State.postValue(Done(coroutine4Data))
                        _scopeState.postValue(Done(scopeData))
                    }

                    (coroutine1Data.children as MutableList<Job>).addAll(this@launch.coroutineContext.job.children.toList())
                    _coroutine1State.postValue(Processing(coroutine1Data))

                    (scopeData.children as MutableList<Job>).addAll(this.coroutineContext.job.children.toList())
                    _scopeState.postValue(Processing(scopeData))
                }

                task5()
                _coroutine1State.postValue(Done(coroutine1Data))
                _mainThreadState.postValue(Done(mainThreadData))
                setExecutionState(ExecutionState.Stop)
            }

            task6()
        }.start()
    }

    fun restartVisualizer() {
        setExecutionState(ExecutionState.Start)
        _coroutine1State.value = Stop()
        _scopeState.value = Stop()
        _coroutine2State.value = Stop()
        _coroutine3State.value = Stop()
        _task1State.value = Stop()
        _task2State.value = Stop()
        _task3State.value = Stop()
        _task4State.value = Stop()
        _task5State.value = Stop()
        _task6State.value = Stop()
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

    private suspend fun task4() {
        val taskData = TaskData(
            "Fourth Task",
            "3000"
        )
        _task4State.postValue(Processing(taskData))
        delay(3_000)
        _task4State.postValue(Done(taskData))
    }

    private suspend fun task5() {
        val taskData = TaskData(
            "Fifth Task",
            "1000"
        )
        _task5State.postValue(Processing(taskData))
        delay(1_000)
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