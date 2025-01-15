package ir.hrka.kotlin.presentation.ui.screens.point

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.Constants.TAG
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
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
class PointViewModel @Inject constructor(
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
    val updatePointsOnDBResult: MutableStateFlow<Resource<Boolean?>> = _updatePointsOnDBResult
    private val _updateTopicStateOnDBResult: MutableStateFlow<Resource<Boolean?>> =
        MutableStateFlow(Resource.Initial())
    val updateTopicStateOnDBResult: MutableStateFlow<Resource<Boolean?>> =
        _updateTopicStateOnDBResult


    fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    fun setFailedState(state: Boolean) {
        _failedState.value = state
    }

    fun getPointsFromGit(topic: Topic) {
        viewModelScope.launch(io) {
            _points.value = Resource.Loading()
            _points.value = getPointsFromGitUseCase(topic)
        }
    }

    fun getPointsFromDB(topic: Topic) {
        viewModelScope.launch(io) {
            _points.value = Resource.Loading()
            _points.value = getPointsFromDBUseCase(topic)
        }
    }

    fun updatePointsOnDB(topic: Topic, points: List<Point>) {
        viewModelScope.launch(io) {
            _updatePointsOnDBResult.value = Resource.Loading()
            _updatePointsOnDBResult.value = updatePointsOnDBUseCase(points, topic)
        }
    }

    fun updateTopicStateOnDB(topic: Topic) {
        viewModelScope.launch(io) {
            _updateTopicStateOnDBResult.value = Resource.Loading()
            _updateTopicStateOnDBResult.value =
                updateTopicsStateOnDBUseCase(topicsIds = intArrayOf(topic.id), state = false)[0]
        }
    }

    fun getAppVersionCode(): Int? = globalData.appVersionCode
}