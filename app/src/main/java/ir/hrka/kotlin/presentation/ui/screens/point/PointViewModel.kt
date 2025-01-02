package ir.hrka.kotlin.presentation.ui.screens.point

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.PointData
import ir.hrka.kotlin.presentation.GlobalData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class PointViewModel @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val globalData: GlobalData
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
}