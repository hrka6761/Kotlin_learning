package ir.hrka.kotlin.presentation.screens.topic

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.Constants.COROUTINE_VERSION_ID_PREFERENCE_KEY
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_ID
import ir.hrka.kotlin.core.Constants.KOTLIN_COURSE_NAME
import ir.hrka.kotlin.core.Constants.KOTLIN_VERSION_ID_PREFERENCE_KEY
import ir.hrka.kotlin.core.utilities.DataStoreManager
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Loading
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.ExecutionState.Stop
import ir.hrka.kotlin.core.utilities.Resource
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
    private val _topics: MutableStateFlow<Resource<List<Topic>?>> =
        MutableStateFlow(Resource.Initial())
    val topics: StateFlow<Resource<List<Topic>?>> = _topics
    private val _updateTopicsOnDBResult: MutableStateFlow<Resource<Boolean?>> =
        MutableStateFlow(Resource.Initial())
    private val _updateTopicsStateOnDBResult: MutableStateFlow<Resource<Boolean?>> =
        MutableStateFlow(Resource.Initial())
    val appVersionCode: Int = globalData.appVersionCode!!


    fun updateTopicStateInList(id: Int) {
        _topics.value.data?.forEach { topic ->
            if (topic.id == id) {
                topic.hasUpdate = false
                return
            }
        }
    }

    fun getTopics(course: Course?) {
        course?.let {
            initTopicsResult(it)
            initUpdateTopicsOnDBResult(it)
            initUpdateTopicsStateOnDBResult(it)

            if (_executionState.value == Start) {
                setExecutionState(Loading)

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


    private fun initTopicsResult(course: Course) {
        viewModelScope.launch {
            _topics.collect { result ->
                if (_executionState.value != Stop) {
                    when (result) {
                        is Resource.Initial -> {}
                        is Resource.Loading -> {}

                        is Resource.Success -> {
                            if (hasTopicsUpdate(course))
                                result.data?.let { updateTopicsOnDB(course, it) }
                            else
                                setExecutionState(Stop)
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

    private fun initUpdateTopicsOnDBResult(course: Course) {
        viewModelScope.launch {
            _updateTopicsOnDBResult.collect { result ->
                if (_executionState.value != Stop) {
                    when (result) {
                        is Resource.Initial -> {}
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            updateStoredVersionId(course)

                            if (hasTopicsUpdate(course)) {
                                setExecutionState(Stop)
                                return@collect
                            }

                            if (hasTopicsPointsUpdate(course))
                                getTopicsFromDB(course)
                        }

                        is Resource.Error -> {
                            if (hasTopicsPointsUpdate(course))
                                getTopicsFromDB(course)
                            else
                                setExecutionState(Stop)
                        }
                    }
                }
            }
        }
    }

    private fun initUpdateTopicsStateOnDBResult(course: Course) {
        viewModelScope.launch {
            _updateTopicsStateOnDBResult.collect { result ->
                if (_executionState.value != Stop) {
                    when (result) {
                        is Resource.Initial -> {}
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            getTopicsFromDB(course)
                            updateStoredVersionId(course)
                        }

                        is Resource.Error -> {
                            getTopicsFromDB(course)
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
            _topics.value = Resource.Loading()
            _topics.value = getTopicsFromGitUseCase(course)
        }
    }

    private fun getTopicsFromDB(course: Course) {
        viewModelScope.launch(io) {
            _topics.value = Resource.Loading()
            _topics.value = getTopicsFromDBUseCase(course)
        }
    }

    private fun updateTopicsOnDB(course: Course, topics: List<Topic>) {
        viewModelScope.launch(io) {
            _updateTopicsOnDBResult.value = Resource.Loading()
            _updateTopicsOnDBResult.value = updateTopicsOnDBUseCase(topics, course)
        }
    }

    private fun updateTopicsStateOnDB(course: Course) {
        viewModelScope.launch(io) {
            _updateTopicsStateOnDBResult.value = Resource.Loading()

            var successResult: Resource<Boolean?>? = null
            var errorResult: Resource<Boolean?>? = null

            val updatedTopics =
                if (course.courseName == KOTLIN_COURSE_NAME)
                    updatedKotlinTopics.toIntArray()
                else
                    updatedCoroutineTopics.toIntArray()

            updateTopicsStateOnDBUseCase(
                topicsIds = updatedTopics,
                state = true
            ).forEach { result ->
                if (result is Resource.Error)
                    errorResult = result
                else
                    successResult = result
            }

            _updateTopicsStateOnDBResult.value =
                if (errorResult != null) errorResult!! else successResult!!
        }
    }

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