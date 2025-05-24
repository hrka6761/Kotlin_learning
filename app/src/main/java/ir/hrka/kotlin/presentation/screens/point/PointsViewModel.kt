package ir.hrka.kotlin.presentation.screens.point

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Loading
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.ExecutionState.Stop
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.core.utilities.onError
import ir.hrka.kotlin.core.utilities.onLoading
import ir.hrka.kotlin.core.utilities.onSuccess
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
    private val _points: MutableStateFlow<Result<List<Point>?, BaseError>> =
        MutableStateFlow(Result.Initial)
    val points: StateFlow<Result<List<Point>?, BaseError>> = _points
    private val _updateTopicStateOnDBResult: MutableStateFlow<Result<Boolean?, BaseError>> =
        MutableStateFlow(Result.Initial)
    val updateTopicStateOnDBResult: StateFlow<Result<Boolean?, BaseError>> =
        _updateTopicStateOnDBResult
    val appVersionCode: Int = globalData.appVersionCode!!


    fun getPoints(topic: Topic?) {
        if (_executionState.value == Start)
            topic?.let {
                if (topic.hasUpdate)
                    getPointsFromGit(it)
                else
                    getPointsFromDB(it)
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
            getPointsFromGitUseCase(topic)
                .collect { result ->
                    _points.value = result

                    result
                        .onLoading {
                            setExecutionState(Loading)
                        }.onSuccess { points ->
                            if (_executionState.value != Stop)
                                points?.let { updatePointsOnDB(topic, it) }
                        }.onError {
                            if (_executionState.value != Stop) {
                                setExecutionState(Stop)
                                setFailedState(true)
                            }
                        }
                }
        }
    }

    private fun getPointsFromDB(topic: Topic) {
        viewModelScope.launch(io) {
            getPointsFromDBUseCase(topic)
                .collect { result ->
                    _points.value = result

                    result
                        .onLoading {

                        }.onSuccess { _ ->
                            setExecutionState(Stop)
                        }.onError {
                            if (_executionState.value != Stop) {
                                setExecutionState(Stop)
                                setFailedState(true)
                            }
                        }
                }
        }
    }

    private fun updatePointsOnDB(topic: Topic, points: List<Point>) {
        viewModelScope.launch(io) {
            updatePointsOnDBUseCase(points, topic)
                .collect { result ->
                    result
                        .onLoading {

                        }.onSuccess { _ ->
                            if (_executionState.value != Stop)
                                updateTopicStateOnDB(topic)
                        }.onError {
                            setExecutionState(Stop)
                        }
                }
        }
    }

    private fun updateTopicStateOnDB(topic: Topic) {
        viewModelScope.launch(io) {
            updateTopicsStateOnDBUseCase(
                topicsIds = intArrayOf(topic.id),
                state = false
            )
                .collect { result ->
                    _updateTopicStateOnDBResult.value = result

                    result
                        .onLoading {

                        }.onSuccess { _ ->
                            setExecutionState(Stop)
                        }.onError {
                            setExecutionState(Stop)
                        }
                }
        }
    }
}