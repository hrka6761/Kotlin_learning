package ir.hrka.kotlin.presentation.ui.screens.kotlin_topic_points

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.PointData
import ir.hrka.kotlin.domain.usecases.db.kotlin.read.GetDBKotlinTopicPointsUseCase
import ir.hrka.kotlin.domain.usecases.git.kotlin.read.GetGitKotlinTopicPointsUseCase
import ir.hrka.kotlin.domain.usecases.db.kotlin.write.SaveKotlinTopicPointsOnDBUseCase
import ir.hrka.kotlin.domain.usecases.db.kotlin.write.UpdateKotlinTopicsStateUseCase
import ir.hrka.kotlin.presentation.GlobalData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class KotlinTopicPointsViewModel @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val globalData: GlobalData,
    private val getGitKotlinTopicPointsUseCase: GetGitKotlinTopicPointsUseCase,
    private val getDBKotlinTopicPointsUseCase: GetDBKotlinTopicPointsUseCase,
    private val saveKotlinTopicPointsOnDBUseCase: SaveKotlinTopicPointsOnDBUseCase,
    private val updateKotlinTopicsStateUseCase: UpdateKotlinTopicsStateUseCase
) : ViewModel() {

    private val _points: MutableStateFlow<Resource<List<PointData>?>> =
        MutableStateFlow(Resource.Initial())
    val points: StateFlow<Resource<List<PointData>?>> = _points
    private val _executionState: MutableStateFlow<ExecutionState> = MutableStateFlow(Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _saveTopicPointsResult: MutableStateFlow<Resource<Boolean>> =
        MutableStateFlow(Resource.Initial())
    val saveTopicPointsResult: MutableStateFlow<Resource<Boolean>> = _saveTopicPointsResult
    private val _updateTopicsOnDBResult: MutableStateFlow<Resource<Boolean>> =
        MutableStateFlow(Resource.Initial())
    val updateTopicsOnDBResult: MutableStateFlow<Resource<Boolean>> = _updateTopicsOnDBResult
    private val _failedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val failedState: StateFlow<Boolean> = _failedState


    fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    fun setFailedState(state: Boolean) {
        _failedState.value = state
    }

    fun getPointsFromGit(topicName: String) {
        viewModelScope.launch(io) {
            _points.value = Resource.Loading()
            _points.value = getGitKotlinTopicPointsUseCase(topicName)
        }
    }

    fun getPointsFromDatabase(topicName: String) {
        viewModelScope.launch(io) {
            _points.value = Resource.Loading()
            _points.value = getDBKotlinTopicPointsUseCase(topicName)
        }
    }

    fun saveTopicPointsOnDB(topicName: String) {
        viewModelScope.launch(io) {
            _saveTopicPointsResult.value =
                _points.value.data?.let { saveKotlinTopicPointsOnDBUseCase(it, topicName) }!!
        }
    }

    fun updateTopicState(topicId: Int) {
        viewModelScope.launch(io) {
//            _updateTopicsOnDBResult.value =
//                updateKotlinTopicsStateUseCase(topicId, hasContentUpdated = false)
        }
    }
}