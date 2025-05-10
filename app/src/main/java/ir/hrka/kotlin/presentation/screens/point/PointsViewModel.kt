package ir.hrka.kotlin.presentation.screens.point

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Loading
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.ExecutionState.Stop
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.Point
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.usecases.db.points.GetPointsFromDBUseCase
import ir.hrka.kotlin.domain.usecases.db.points.UpdatePointsOnDBUseCase
import ir.hrka.kotlin.domain.usecases.db.topics.UpdateTopicsStateOnDBUseCase
import ir.hrka.kotlin.domain.usecases.git.GetPointsFromGitUseCase
import ir.hrka.kotlin.presentation.GlobalData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class PointsViewModel @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val globalData: GlobalData,
    private val getPointsFromGitUseCase: GetPointsFromGitUseCase,
    private val getPointsFromDBUseCase: GetPointsFromDBUseCase,
    private val updatePointsOnDBUseCase: UpdatePointsOnDBUseCase,
    private val updateTopicsStateOnDBUseCase: UpdateTopicsStateOnDBUseCase
) : ViewModel() {

    private val _executionState: MutableStateFlow<ExecutionState> = MutableStateFlow(Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _failedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val failedState: StateFlow<Boolean> = _failedState
    private val _points: MutableStateFlow<Resource<List<Point>?>> =
        MutableStateFlow(Resource.Initial())
    val points: StateFlow<Resource<List<Point>?>> = _points
    private val _updatePointsOnDBResult: MutableStateFlow<Resource<Boolean?>> =
        MutableStateFlow(Resource.Initial())
    private val _updateTopicStateOnDBResult: MutableStateFlow<Resource<Boolean?>> =
        MutableStateFlow(Resource.Initial())
    val updateTopicStateOnDBResult: StateFlow<Resource<Boolean?>> = _updateTopicStateOnDBResult


    fun getPoints(topic: Topic?) {
        initPointsResult(topic)
        initUpdatePointsOnDBResult(topic)
        initUpdateTopicStateOnDBResult()

        if (_executionState.value == Start) {
            setExecutionState(Loading)
            topic?.let {
                if (topic.hasUpdate)
                    getPointsFromGit(it)
                else
                    getPointsFromDB(it)
            }
        }
    }


    private fun initPointsResult(topic: Topic?) {
        viewModelScope.launch {
            _points.collect { result ->
                if (_executionState.value != Stop) {
                    when (result) {
                        is Resource.Initial -> {}
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            topic?.let {
                                if (topic.hasUpdate)
                                    result.data?.let { updatePointsOnDB(topic, it) }
                                else
                                    setExecutionState(Stop)
                            }

                        }

                        is Resource.Error -> {
                            setExecutionState(Stop)
                            setFailedState(true)
                        }
                    }
                }
            }
        }
    }

    private fun initUpdatePointsOnDBResult(topic: Topic?) {
        viewModelScope.launch {
            _updatePointsOnDBResult.collect { result ->
                if (_executionState.value != Stop) {
                    when (result) {
                        is Resource.Initial -> {}
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            topic?.let { updateTopicStateOnDB(it) }
                        }

                        is Resource.Error -> {
                            setExecutionState(Stop)
                        }
                    }
                }
            }
        }
    }

    private fun initUpdateTopicStateOnDBResult() {
        viewModelScope.launch {
            _updateTopicStateOnDBResult.collect { result ->
                if (_executionState.value != Stop) {
                    when (result) {
                        is Resource.Initial -> {}
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            setExecutionState(Stop)
                        }

                        is Resource.Error -> {
                            setExecutionState(Stop)
                        }
                    }
                }
            }
        }
    }

    private fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    private fun setFailedState(state: Boolean) {
        _failedState.value = state
    }

    private fun getPointsFromGit(topic: Topic) {
        viewModelScope.launch(io) {
            _points.value = Resource.Loading()
            _points.value = getPointsFromGitUseCase(topic)
        }
    }

    private fun getPointsFromDB(topic: Topic) {
        viewModelScope.launch(io) {
            _points.value = Resource.Loading()
            _points.value = getPointsFromDBUseCase(topic)
        }
    }

    private fun updatePointsOnDB(topic: Topic, points: List<Point>) {
        viewModelScope.launch(io) {
            _updatePointsOnDBResult.value = Resource.Loading()
            _updatePointsOnDBResult.value = updatePointsOnDBUseCase(points, topic)
        }
    }

    private fun updateTopicStateOnDB(topic: Topic) {
        viewModelScope.launch(io) {
            _updateTopicStateOnDBResult.value = Resource.Loading()
            _updateTopicStateOnDBResult.value =
                updateTopicsStateOnDBUseCase(topicsIds = intArrayOf(topic.id), state = false)[0]
        }
    }
}