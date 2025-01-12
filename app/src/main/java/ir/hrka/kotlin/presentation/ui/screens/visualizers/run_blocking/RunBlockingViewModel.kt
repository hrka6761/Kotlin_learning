package ir.hrka.kotlin.presentation.ui.screens.visualizers.run_blocking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Stop
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Processing
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Done
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.CoroutineData
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.TaskData
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ThreadData
import ir.hrka.kotlin.presentation.GlobalData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.job
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RunBlockingViewModel @Inject constructor(
    private val globalData: GlobalData
) : ViewModel() {

    private val _executionState: MutableStateFlow<ExecutionState> =
        MutableStateFlow(ExecutionState.Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _mainThreadState: MutableLiveData<ComponentState<ThreadData>> =
        MutableLiveData(Stop())
    val mainThreadState: LiveData<ComponentState<ThreadData>> = _mainThreadState
    private val _coroutineState: MutableLiveData<ComponentState<CoroutineData>> =
        MutableLiveData(Stop())
    val coroutineState: LiveData<ComponentState<CoroutineData>> = _coroutineState
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
    private val mainThreadData = ThreadData("2", "main")

    fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    fun runAllTasks() {
//        I had to use another thread to visualize and manually named the thread name and ID here.
        Thread {
            _mainThreadState.postValue(Processing(mainThreadData))
            task1()
            runBlockingExecution()
            task4()
        }.start()
    }

    fun restartVisualizer() {
        setExecutionState(ExecutionState.Start)
        _coroutineState.value = Stop()
        _task1State.value = Stop()
        _task2State.value = Stop()
        _task3State.value = Stop()
        _task4State.value = Stop()
        runAllTasks()
    }

    private fun runBlockingExecution() {
        runBlocking {
            val coroutineData = CoroutineData(
                coroutineName = Thread.currentThread().name.split(" ").last(),
                threadContext = Thread.currentThread().name.split(" ").first(),
                jobContext = this.coroutineContext.job.toString()
            )
            _coroutineState.postValue(Processing(coroutineData))
            task2()
            task3()
            _coroutineState.postValue(Done(coroutineData))
            _mainThreadState.postValue(Done(mainThreadData))
            setExecutionState(ExecutionState.Stop)
        }
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

    private fun task4() {
        val taskData = TaskData(
            "Fourth Task",
            "3000"
        )
        _task4State.postValue(Processing(taskData))
        Thread.sleep(3_000)
        _task4State.postValue(Done(taskData))
    }
}