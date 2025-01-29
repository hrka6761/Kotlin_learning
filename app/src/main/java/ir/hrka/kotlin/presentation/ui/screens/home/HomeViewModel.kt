package ir.hrka.kotlin.presentation.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_ID
import ir.hrka.kotlin.core.Constants.TAG
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.usecases.db.courses.GetCoursesFromDBUseCase
import ir.hrka.kotlin.domain.usecases.db.courses.UpdateCoursesOnDBUseCase
import ir.hrka.kotlin.domain.usecases.git.GetCoursesFromGitUseCase
import ir.hrka.kotlin.domain.usecases.preference.SaveCoursesVersionIdUseCase
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
    private val saveCoursesVersionIdUseCase: SaveCoursesVersionIdUseCase
) : ViewModel() {

    private val hasCoursesUpdate = globalData.hasCoursesUpdate
    private val lastVersionId = globalData.lastVersionId
    private val _executionState: MutableStateFlow<ExecutionState> = MutableStateFlow(Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _failedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val failedState: StateFlow<Boolean> = _failedState
    private val _courses: MutableStateFlow<Resource<List<Course>?>> =
        MutableStateFlow(Resource.Initial())
    val courses: StateFlow<Resource<List<Course>?>> = _courses
    private val _saveCourseOnDBResult: MutableStateFlow<Resource<Boolean?>> =
        MutableStateFlow(Resource.Initial())
    private val _updateCoursesVersionIdResult: MutableStateFlow<Resource<Boolean?>> =
        MutableStateFlow(Resource.Initial())


    fun getCourses() {
        initGetCoursesResult()
        initSaveCoursesOnDBResult()
        initUpdateCoursesVersionIdResult()

        if (_executionState.value == Start) {
            setExecutionState(ExecutionState.Loading)
            if (hasCoursesUpdate)
                getCoursesFromGit()
            else
                getCoursesFromDB()
        }
    }


    private fun initGetCoursesResult() {
        viewModelScope.launch {
            _courses.collect { result ->
                if (_executionState.value != ExecutionState.Stop)
                    when (result) {
                        is Resource.Initial -> {}
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            if (hasCoursesUpdate)
                                _courses.value.data?.let { saveCoursesOnDB(it) }
                            else
                                setExecutionState(ExecutionState.Stop)
                        }

                        is Resource.Error -> {
                            setExecutionState(ExecutionState.Stop)
                            setFailedState(true)
                        }
                    }
            }
        }
    }

    private fun initSaveCoursesOnDBResult() {
        viewModelScope.launch {
            _saveCourseOnDBResult.collect { result ->
                if (_executionState.value != ExecutionState.Stop)
                    when (result) {
                        is Resource.Initial -> {}
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            updateCoursesVersionId()
                        }

                        is Resource.Error -> {
                            setExecutionState(ExecutionState.Stop)
                        }
                    }
            }
        }
    }

    private fun initUpdateCoursesVersionIdResult() {
        viewModelScope.launch {
            _updateCoursesVersionIdResult.collect { result ->
                if (_executionState.value != ExecutionState.Stop)
                    when (result) {
                        is Resource.Initial -> {}
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            setExecutionState(ExecutionState.Stop)
                            updateCoursesVersionIdInGlobalData()
                        }

                        is Resource.Error -> {
                            setExecutionState(ExecutionState.Stop)
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

    private fun getCoursesFromGit() {
        viewModelScope.launch(io) {
            _courses.value = Resource.Loading()
            _courses.value = getCoursesFromGitUseCase()
        }
    }

    private fun getCoursesFromDB() {
        viewModelScope.launch(io) {
            _courses.value = Resource.Loading()
            _courses.value = getCoursesFromDBUseCase()
        }
    }

    private fun saveCoursesOnDB(courses: List<Course>) {
        viewModelScope.launch(io) {
            _saveCourseOnDBResult.value = Resource.Loading()
            _saveCourseOnDBResult.value = updateCoursesOnDBUseCase(courses)
        }
    }

    private fun updateCoursesVersionId() {
        viewModelScope.launch(io) {
            val versionId = lastVersionId ?: DEFAULT_VERSION_ID
            _updateCoursesVersionIdResult.value = Resource.Loading()
            _updateCoursesVersionIdResult.value = saveCoursesVersionIdUseCase(versionId)
        }
    }

    private fun updateCoursesVersionIdInGlobalData() {
        globalData.hasCoursesUpdate = false
    }
}