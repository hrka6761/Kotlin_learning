package ir.hrka.kotlin.presentation.screens.topic

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.Constants.COROUTINE_VERSION_ID_PREFERENCE_KEY
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_ID
import ir.hrka.kotlin.core.Constants.KOTLIN_COURSE_NAME
import ir.hrka.kotlin.core.Constants.KOTLIN_VERSION_ID_PREFERENCE_KEY
import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.core.utilities.DataStoreManager
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Loading
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.ExecutionState.Stop
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.core.utilities.onError
import ir.hrka.kotlin.core.utilities.onLoading
import ir.hrka.kotlin.core.utilities.onSuccess
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.usecases.db.topics.GetTopicsFromDBUseCase
import ir.hrka.kotlin.domain.usecases.git.GetTopicsFromGitUseCase
import ir.hrka.kotlin.domain.usecases.db.topics.UpdateTopicsOnDBUseCase
import ir.hrka.kotlin.domain.usecases.db.topics.UpdateTopicsStateOnDBUseCase
import ir.hrka.kotlin.presentation.GlobalData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class TopicViewModel @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val globalData: GlobalData,
    private val getTopicsFromGitUseCase: GetTopicsFromGitUseCase,
    private val getTopicsFromDBUseCase: GetTopicsFromDBUseCase,
    private val updateTopicsOnDBUseCase: UpdateTopicsOnDBUseCase,
    private val updateTopicsStateOnDBUseCase: UpdateTopicsStateOnDBUseCase,
    private val dataStoreManager: DataStoreManager
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
    private val _topics: MutableStateFlow<Result<List<Topic>?, Errors>> =
        MutableStateFlow(Result.Initial)
    val topics: StateFlow<Result<List<Topic>?, Errors>> = _topics
    val appVersionCode: Int = globalData.appVersionCode!!


    fun updateTopicStateInList(id: Int) {
        (_topics.value as? Result.Success)
            ?.data
            ?.forEach { topic ->
                if (topic.id == id) {
                    topic.hasUpdate = false
                    return
                }
            }
    }

    fun getTopics(course: Course?) {
        course?.let {
            if (_executionState.value == Start) {
                if (hasTopicsUpdate(it)) {
                    getTopicsFromGit(it)
                    return
                }

                if (hasTopicsPointsUpdate(it)) {
                    updateTopicsStateOnDB(it)
                    return
                }

                getTopicsFromDB(it)
            }
        }
    }

    private fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    private fun setFailedState(state: Boolean) {
        _failedState.value = state
    }

    private fun hasTopicsUpdate(course: Course): Boolean {
        return if (course.courseName == KOTLIN_COURSE_NAME)
            hasKotlinTopicsUpdate
        else
            hasCoroutineTopicsUpdate
    }

    private fun hasTopicsPointsUpdate(course: Course): Boolean {
        return if (course.courseName == KOTLIN_COURSE_NAME)
            hasKotlinTopicsPointsUpdate
        else
            hasCoroutineTopicsPointsUpdate
    }

    private fun getTopicsFromGit(course: Course) {
        viewModelScope.launch(io) {
            getTopicsFromGitUseCase(course)
                .collect { result ->
                    _topics.value = result

                    result
                        .onLoading {
                            setExecutionState(Loading)
                        }.onSuccess { topics ->
                            if (_executionState.value != Stop)
                                topics?.let { updateTopicsOnDB(course, it) }
                        }.onError {
                            if (_executionState.value != Stop) {
                                setExecutionState(Stop)
                                setFailedState(true)
                            }
                        }
                }
        }
    }

    private fun getTopicsFromDB(course: Course) {
        viewModelScope.launch(io) {
            getTopicsFromDBUseCase(course)
                .collect { result ->
                    _topics.value = result

                    result
                        .onLoading {

                        }.onSuccess { topics ->
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

    private fun updateTopicsOnDB(course: Course, topics: List<Topic>) {
        viewModelScope.launch(io) {
            updateTopicsOnDBUseCase(topics, course)
                .collect { result ->
                    result
                        .onLoading {

                        }.onSuccess { topics ->
                            if (_executionState.value != Stop) {
                                updateStoredVersionId(course)
                                setExecutionState(Stop)
                            }
                        }.onError {
                            setExecutionState(Stop)
                        }
                }
        }
    }

    private fun updateTopicsStateOnDB(course: Course) {
        val updatedTopics = getUpdatedTopics(course)
        var successfulUpdatesNum = 0

        viewModelScope.launch(io) {
            updateTopicsStateOnDBUseCase(
                topicsIds = updatedTopics,
                state = true
            )
                .collect { result ->
                    result
                        .onLoading {

                        }.onSuccess { _ ->
                            if (_executionState.value != Stop) {
                                successfulUpdatesNum++

                                if (successfulUpdatesNum >= updatedTopics.size) {
                                    getTopicsFromDB(course)
                                    updateStoredVersionId(course)
                                }
                            }
                        }.onError {
                            if (_executionState.value != Stop)
                                getTopicsFromDB(course)
                        }
                }
        }
    }

    private fun getUpdatedTopics(course: Course) =
        if (course.courseName == KOTLIN_COURSE_NAME)
            updatedKotlinTopics.toIntArray()
        else
            updatedCoroutineTopics.toIntArray()

    private fun updateStoredVersionId(course: Course) {
        viewModelScope.launch(io) {
            val versionId = lastVersionId ?: DEFAULT_VERSION_ID
            if (course.courseName == KOTLIN_COURSE_NAME) {
                dataStoreManager.saveData(
                    intPreferencesKey(KOTLIN_VERSION_ID_PREFERENCE_KEY),
                    versionId
                )
                globalData.hasKotlinTopicsUpdate = false
                globalData.hasKotlinTopicsPointsUpdate = false
            } else {
                dataStoreManager.saveData(
                    intPreferencesKey(COROUTINE_VERSION_ID_PREFERENCE_KEY),
                    versionId
                )
                globalData.hasCoroutineTopicsUpdate = false
                globalData.hasCoroutineTopicsPointsUpdate = false
            }
        }
    }
}