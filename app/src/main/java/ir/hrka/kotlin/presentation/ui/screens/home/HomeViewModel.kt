package ir.hrka.kotlin.presentation.ui.screens.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_ID
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

    val hasCoursesUpdate = globalData.hasCoursesUpdate
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
    val saveCourseOnDBResult: StateFlow<Resource<Boolean?>> = _saveCourseOnDBResult
    private val _updateCoursesVersionIdResult: MutableStateFlow<Resource<Boolean?>> =
        MutableStateFlow(Resource.Initial())
    val updateCoursesVersionIdResult: StateFlow<Resource<Boolean?>> = _updateCoursesVersionIdResult


    fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    fun setFailedState(state: Boolean) {
        _failedState.value = state
    }

    fun openUrl(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    fun getCoursesFromGit() {
        viewModelScope.launch(io) {
            _courses.value = Resource.Loading()
            _courses.value = getCoursesFromGitUseCase()
        }
    }

    fun getCoursesFromDB() {
        viewModelScope.launch(io) {
            _courses.value = Resource.Loading()
            _courses.value = getCoursesFromDBUseCase()
        }
    }

    fun saveCoursesOnDB(courses: List<Course>) {
        viewModelScope.launch(io) {
            _saveCourseOnDBResult.value = Resource.Loading()
            _saveCourseOnDBResult.value = updateCoursesOnDBUseCase(courses)
        }
    }

    fun updateCoursesVersionId() {
        viewModelScope.launch(io) {
            val versionId = lastVersionId ?: DEFAULT_VERSION_ID
            _updateCoursesVersionIdResult.value = Resource.Loading()
            _updateCoursesVersionIdResult.value = saveCoursesVersionIdUseCase(versionId)
        }
    }

    fun updateCoursesVersionIdInGlobalData() {
        globalData.hasCoursesUpdate = false
    }
}