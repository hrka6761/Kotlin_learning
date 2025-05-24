package ir.hrka.kotlin.presentation.screens.home

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.Constants.COURSES_VERSION_ID_PREFERENCE_KEY
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_ID
import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.utilities.DataStoreManager
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Stop
import ir.hrka.kotlin.core.utilities.ExecutionState.Loading
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.core.utilities.onError
import ir.hrka.kotlin.core.utilities.onLoading
import ir.hrka.kotlin.core.utilities.onSuccess
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.usecases.db.courses.GetCoursesFromDBUseCase
import ir.hrka.kotlin.domain.usecases.db.courses.UpdateCoursesOnDBUseCase
import ir.hrka.kotlin.domain.usecases.git.GetCoursesFromGitUseCase
import ir.hrka.kotlin.presentation.GlobalData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val globalData: GlobalData,
    private val getCoursesFromGitUseCase: GetCoursesFromGitUseCase,
    private val getCoursesFromDBUseCase: GetCoursesFromDBUseCase,
    private val updateCoursesOnDBUseCase: UpdateCoursesOnDBUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val hasCoursesUpdate = globalData.hasCoursesUpdate
    private val lastVersionId = globalData.lastVersionId
    private val _executionState: MutableStateFlow<ExecutionState> = MutableStateFlow(Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _failedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val failedState: StateFlow<Boolean> = _failedState
    private val _courses: MutableStateFlow<Result<List<Course>?, BaseError>> =
        MutableStateFlow(Result.Initial)
    val courses: StateFlow<Result<List<Course>?, BaseError>> = _courses


    fun getCourses() {
        if (_executionState.value == Start)
            if (hasCoursesUpdate)
                getCoursesFromGit()
            else
                getCoursesFromDB()
    }

    private fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    private fun setFailedState(state: Boolean) {
        _failedState.value = state
    }

    private fun getCoursesFromGit() {
        viewModelScope.launch(io) {
            getCoursesFromGitUseCase()
                .collect { result ->
                    _courses.value = result
                    result
                        .onLoading {
                            setExecutionState(Loading)
                        }.onSuccess { courses ->
                            if (_executionState.value != Stop)
                                courses?.let { saveCoursesOnDB(it) }
                        }.onError {
                            if (_executionState.value != Stop) {
                                setExecutionState(Stop)
                                setFailedState(true)
                            }
                        }
                }
        }
    }

    private fun getCoursesFromDB() {
        viewModelScope.launch(io) {
            getCoursesFromDBUseCase()
                .collect { result ->
                    _courses.value = result

                    result
                        .onLoading {
                            setExecutionState(Loading)
                        }.onSuccess { courses ->
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

    private fun saveCoursesOnDB(courses: List<Course>) {
        viewModelScope.launch(io) {
            updateCoursesOnDBUseCase(courses)
                .collect { result ->
                    result
                        .onLoading {

                        }.onSuccess { _ ->
                            updateCoursesVersion()
                            setExecutionState(Stop)
                        }.onError {
                            setExecutionState(Stop)
                        }
                }
        }
    }

    private suspend fun updateCoursesVersion() {
        val versionId = lastVersionId ?: DEFAULT_VERSION_ID
        dataStoreManager.saveData(
            intPreferencesKey(COURSES_VERSION_ID_PREFERENCE_KEY),
            versionId
        )
        globalData.hasCoursesUpdate = false
    }
}