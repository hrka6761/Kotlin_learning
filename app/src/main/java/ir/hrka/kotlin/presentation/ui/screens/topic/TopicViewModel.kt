package ir.hrka.kotlin.presentation.ui.screens.topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_ID
import ir.hrka.kotlin.core.utilities.Course
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.usecases.db.topics.GetDBTopicsUseCase
import ir.hrka.kotlin.domain.usecases.db.topics.RemoveDBTopicsUseCase
import ir.hrka.kotlin.domain.usecases.git.GetGitKotlinTopicsUseCase
import ir.hrka.kotlin.domain.usecases.db.topics.SaveTopicsOnDBUseCase
import ir.hrka.kotlin.domain.usecases.preference.SaveKotlinVersionIdUseCase
import ir.hrka.kotlin.domain.usecases.db.topics.UpdateTopicsStateOnDBUseCase
import ir.hrka.kotlin.presentation.GlobalData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class TopicViewModel @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val globalData: GlobalData,
    private val getGitKotlinTopicsUseCase: GetGitKotlinTopicsUseCase,
    private val getDBTopicsUseCase: GetDBTopicsUseCase,
    private val saveTopicsOnDBUseCase: SaveTopicsOnDBUseCase,
    private val removeDBTopicsUseCase: RemoveDBTopicsUseCase,
    private val saveKotlinVersionIdUseCase: SaveKotlinVersionIdUseCase,
    private val updateTopicsStateOnDBUseCase: UpdateTopicsStateOnDBUseCase
) : ViewModel() {

    val hasKotlinTopicsUpdate = globalData.hasKotlinTopicsUpdate
    val hasKotlinTopicsPointsUpdate = globalData.hasKotlinTopicsPointsUpdate
    val updatedKotlinTopics = globalData.updatedKotlinTopics
    private val lastVersionId = globalData.lastVersionId
    private val _executionState: MutableStateFlow<ExecutionState> = MutableStateFlow(Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _failedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val failedState: StateFlow<Boolean> = _failedState
    private val _topics: MutableStateFlow<Resource<List<Topic>?>> =
        MutableStateFlow(Resource.Initial())
    val topics: StateFlow<Resource<List<Topic>?>> = _topics
    private val _saveKotlinTopicsResult: MutableStateFlow<Resource<Boolean?>> =
        MutableStateFlow(Resource.Initial())
    val saveKotlinTopicsResult: StateFlow<Resource<Boolean?>> = _saveKotlinTopicsResult
    private val _updateKotlinTopicsOnDBResult: MutableStateFlow<Resource<Boolean?>> =
        MutableStateFlow(Resource.Initial())
    val updateKotlinTopicsOnDBResult: MutableStateFlow<Resource<Boolean?>> =
        _updateKotlinTopicsOnDBResult
    private val _updateKotlinVersionIdResult: MutableStateFlow<Resource<Boolean?>> =
        MutableStateFlow(Resource.Initial())
    val updateKotlinVersionIdResult: StateFlow<Resource<Boolean?>> = _updateKotlinVersionIdResult


    fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    fun setFailedState(state: Boolean) {
        _failedState.value = state
    }

    fun getKotlinTopicsFromGit() {
        viewModelScope.launch(io) {
            _topics.value = Resource.Loading()
            _topics.value = getGitKotlinTopicsUseCase()
        }
    }

    fun getKotlinTopicsFromDB(course: Course) {
        viewModelScope.launch(io) {
            _topics.value = Resource.Loading()
            _topics.value = getDBTopicsUseCase(course)
        }
    }

    fun saveKotlinTopicsOnDB(course: Course, topics: List<Topic>) {
        viewModelScope.launch(io) {
            _saveKotlinTopicsResult.value = Resource.Loading()

            val removeDiffered = async { removeDBTopicsUseCase(course) }
            val removeResult = removeDiffered.await()

            if (removeResult is Resource.Error) {
                _saveKotlinTopicsResult.value = removeResult
                return@launch
            }

            _saveKotlinTopicsResult.value = saveTopicsOnDBUseCase(topics)
        }
    }

    fun updateKotlinTopicsOnDB() {
        viewModelScope.launch(io) {
            _updateKotlinTopicsOnDBResult.value = Resource.Loading()

            var successResult: Resource<Boolean?>? = null
            var errorResult: Resource<Boolean?>? = null

            updateTopicsStateOnDBUseCase(
                topicsIds = updatedKotlinTopics.toIntArray(),
                state = false
            ).forEach { result ->
                if (result is Resource.Error)
                    errorResult = result
                else
                    successResult = result
            }

            _updateKotlinTopicsOnDBResult.value =
                if (errorResult != null) errorResult!! else successResult!!
        }
    }

    fun updateKotlinVersionId() {
        viewModelScope.launch(io) {
            val versionId = lastVersionId ?: DEFAULT_VERSION_ID
            _updateKotlinVersionIdResult.value = Resource.Loading()
            _updateKotlinVersionIdResult.value = saveKotlinVersionIdUseCase(versionId)
        }
    }

    fun updateKotlinVersionIdInGlobalData() {
        globalData.hasKotlinTopicsUpdate = false
        globalData.hasKotlinTopicsPointsUpdate = false
    }
}