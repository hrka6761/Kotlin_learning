package ir.hrka.kotlin.presentation.ui.screens.topic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_ID
import ir.hrka.kotlin.core.Constants.KOTLIN_COURSE_NAME
import ir.hrka.kotlin.core.Constants.TAG
import ir.hrka.kotlin.core.utilities.Course
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.usecases.db.topics.GetDBTopicsUseCase
import ir.hrka.kotlin.domain.usecases.db.topics.RemoveDBTopicsUseCase
import ir.hrka.kotlin.domain.usecases.git.GetGitTopicsUseCase
import ir.hrka.kotlin.domain.usecases.db.topics.SaveTopicsOnDBUseCase
import ir.hrka.kotlin.domain.usecases.preference.SaveKotlinVersionIdUseCase
import ir.hrka.kotlin.domain.usecases.db.topics.UpdateTopicsStateOnDBUseCase
import ir.hrka.kotlin.domain.usecases.preference.SaveCoroutineVersionIdUseCase
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
    private val getGitTopicsUseCase: GetGitTopicsUseCase,
    private val getDBTopicsUseCase: GetDBTopicsUseCase,
    private val saveTopicsOnDBUseCase: SaveTopicsOnDBUseCase,
    private val removeDBTopicsUseCase: RemoveDBTopicsUseCase,
    private val saveKotlinVersionIdUseCase: SaveKotlinVersionIdUseCase,
    private val saveCoroutineVersionIdUseCase: SaveCoroutineVersionIdUseCase,
    private val updateTopicsStateOnDBUseCase: UpdateTopicsStateOnDBUseCase
) : ViewModel() {

    private val hasKotlinTopicsUpdate = globalData.hasKotlinTopicsUpdate
    private val hasKotlinTopicsPointsUpdate = globalData.hasKotlinTopicsPointsUpdate
    private val updatedKotlinTopics = globalData.updatedKotlinTopics
    private val hasCoroutineTopicsUpdate = globalData.hasCoroutineTopicsUpdate
    private val hasCoroutineTopicsPointsUpdate = globalData.hasCoroutineTopicsPointsUpdate
    private val updatedCoroutineTopics = globalData.updatedCoroutineTopics
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
    private val _updateVersionIdResult: MutableStateFlow<Resource<Boolean?>> =
        MutableStateFlow(Resource.Initial())
    val updateVersionIdResult: StateFlow<Resource<Boolean?>> = _updateVersionIdResult


    fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    fun setFailedState(state: Boolean) {
        _failedState.value = state
    }

    fun hasTopicsUpdate(course: Course): Boolean {
        return if (course.courseName == KOTLIN_COURSE_NAME)
            hasKotlinTopicsUpdate
        else
            hasCoroutineTopicsUpdate
    }

    fun hasTopicsPointsUpdate(course: Course): Boolean {
        return if (course.courseName == KOTLIN_COURSE_NAME)
            hasKotlinTopicsPointsUpdate
        else
            hasCoroutineTopicsPointsUpdate
    }

    fun getTopicsFromGit(course: Course) {
        viewModelScope.launch(io) {
            _topics.value = Resource.Loading()
            _topics.value = getGitTopicsUseCase(course)
        }
    }

    fun getTopicsFromDB(course: Course) {
        viewModelScope.launch(io) {
            _topics.value = Resource.Loading()
            _topics.value = getDBTopicsUseCase(course)
        }
    }

    fun saveTopicsOnDB(course: Course, topics: List<Topic>) {
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

    fun updateTopicsOnDB(course: Course) {
        viewModelScope.launch(io) {
            _updateKotlinTopicsOnDBResult.value = Resource.Loading()

            var successResult: Resource<Boolean?>? = null
            var errorResult: Resource<Boolean?>? = null

            val updatedTopics =
                if (course.courseName == KOTLIN_COURSE_NAME)
                    updatedKotlinTopics.toIntArray()
                else
                    updatedCoroutineTopics.toIntArray()

            updateTopicsStateOnDBUseCase(
                topicsIds = updatedTopics,
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

    fun updateVersionId(course: Course) {
        viewModelScope.launch(io) {
            val versionId = lastVersionId ?: DEFAULT_VERSION_ID
            _updateVersionIdResult.value = Resource.Loading()
            _updateVersionIdResult.value =
                if (course.courseName == KOTLIN_COURSE_NAME)
                    saveKotlinVersionIdUseCase(versionId)
                else
                    saveCoroutineVersionIdUseCase(versionId)
        }
    }

    fun updateVersionIdInGlobalData(course: Course) {
        if (course.courseName == KOTLIN_COURSE_NAME) {
            globalData.hasKotlinTopicsUpdate = false
            globalData.hasKotlinTopicsPointsUpdate = false
        } else {
            globalData.hasCoroutineTopicsUpdate = false
            globalData.hasCoroutineTopicsPointsUpdate = false
        }
    }
}