package ir.hrka.kotlin.presentation.ui.screens.visualizers.coroutine_context

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Done
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Processing
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Stop
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.CoroutineData
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
class CoroutineContextViewModel @Inject constructor(
    private val globalData: GlobalData
) : ViewModel() {

    private val _executionState: MutableStateFlow<ExecutionState> =
        MutableStateFlow(ExecutionState.Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _mainThreadState: MutableLiveData<ComponentState<ThreadData>> =
        MutableLiveData(Stop())
    val mainThreadState: LiveData<ComponentState<ThreadData>> = _mainThreadState
    private val mainThreadData = ThreadData("2", "main")
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


    fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    fun runAllTasks() {
//        I had to use another thread to visualize and manually named the thread name and ID here.
        Thread {
            _mainThreadState.postValue(Processing(mainThreadData))

            val scopeJob = Job()
            val threads = Dispatchers.Main
            val coroutineName = CoroutineName("outer_coroutine")

            val innerCoroutineJob1 = Job()
            val innerCoroutineThreads1 = Dispatchers.IO
            val innerCoroutineName1 = CoroutineName("first_inner_coroutine")

            val innerCoroutineJob2 = Job()
            val innerCoroutineThreads2 = Dispatchers.Default
            val innerCoroutineName2 = CoroutineName("second_inner_coroutine")

            CoroutineScope(scopeJob + threads + coroutineName).launch {
                val coroutine1Data = CoroutineData(
                    coroutineName = Thread.currentThread().name.split(" ").last(),
                    thread = Thread.currentThread().name.split(" ").first(),
                    job = this.coroutineContext.job,
                    parentJob = this.coroutineContext.job.parent,
                    children = mutableListOf()
                )
                _coroutine1State.postValue(Processing(coroutine1Data))

                launch(innerCoroutineJob1 + innerCoroutineThreads1 + innerCoroutineName1) {
                    val coroutine2Data = CoroutineData(
                        coroutineName = Thread.currentThread().name.split(" ").last(),
                        thread = Thread.currentThread().name.split(" ").first(),
                        job = this.coroutineContext.job,
                        parentJob = this.coroutineContext.job.parent,
                        children = this.coroutineContext.job.children.toList()
                    )
                    _coroutine2State.postValue(Processing(coroutine2Data))
                    task1()
                    _coroutine2State.postValue(Done(coroutine2Data))
                    _coroutine1State.postValue(Done(coroutine1Data))
                    _mainThreadState.postValue(Done(mainThreadData))
                    setExecutionState(ExecutionState.Stop)
                }

                launch(innerCoroutineJob2 + innerCoroutineThreads2 + innerCoroutineName2) {
                    val coroutine3Data = CoroutineData(
                        coroutineName = Thread.currentThread().name.split(" ").last(),
                        thread = Thread.currentThread().name.split(" ").first(),
                        job = this.coroutineContext.job,
                        parentJob = this.coroutineContext.job.parent,
                        children = this.coroutineContext.job.children.toList()
                    )
                    _coroutine3State.postValue(Processing(coroutine3Data))
                    task2()
                    _coroutine3State.postValue(Done(coroutine3Data))
                }

            }
        }.start()
    }

    fun restartVisualizer() {
        setExecutionState(ExecutionState.Start)
        _coroutine1State.value = Stop()
        _coroutine2State.value = Stop()
        _task1State.value = Stop()
        _task2State.value = Stop()
        runAllTasks()
    }


    private suspend fun task1() {
        val taskData = TaskData(
            "First Task",
            "4000"
        )
        _task1State.postValue(Processing(taskData))
        delay(4_000)
        _task1State.postValue(Done(taskData))
    }

    private suspend fun task2() {
        val taskData = TaskData(
            "Second Task",
            "2000"
        )
        _task2State.postValue(Processing(taskData))
        delay(2_000)
        _task2State.postValue(Done(taskData))
    }
}