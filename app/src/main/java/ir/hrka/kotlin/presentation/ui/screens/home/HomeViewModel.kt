package ir.hrka.kotlin.presentation.ui.screens.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.usecases.db.cources.ClearDBCoursesUseCase
import ir.hrka.kotlin.domain.usecases.db.cources.GetDBCoursesUseCase
import ir.hrka.kotlin.domain.usecases.db.cources.PutDBCoursesUseCase
import ir.hrka.kotlin.domain.usecases.git.kotlin.read.GetGitCoursesUseCase
import ir.hrka.kotlin.presentation.GlobalData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val globalData: GlobalData,
    private val getGitCoursesUseCase: GetGitCoursesUseCase,
    private val getDBCoursesUseCase: GetDBCoursesUseCase,
    private val clearDBCoursesUseCase: ClearDBCoursesUseCase,
    private val putDBCoursesUseCase: PutDBCoursesUseCase
) : ViewModel() {

    val hasCoursesUpdate = globalData._hasCoursesUpdate
    val coursesVersionId = globalData._coursesVersionId
    private val _courses: MutableStateFlow<Resource<List<Course>?>> =
        MutableStateFlow(Resource.Initial())
    val courses: StateFlow<Resource<List<Course>?>> = _courses
    private val _executionState: MutableStateFlow<ExecutionState> = MutableStateFlow(Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _failedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val failedState: StateFlow<Boolean> = _failedState
    private val _saveCourseOnDBResult: MutableStateFlow<Resource<Boolean>> =
        MutableStateFlow(Resource.Initial())
    val saveCourseOnDBResult: StateFlow<Resource<Boolean>> = _saveCourseOnDBResult


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

    fun getCoursesListFromGit() {
        viewModelScope.launch(io) {
            _courses.value = Resource.Loading()
            _courses.value = getGitCoursesUseCase()
        }
    }

    fun getCoursesListFromDB() {
        viewModelScope.launch(io) {
            _courses.value = Resource.Loading()
            _courses.value = getDBCoursesUseCase()
        }
    }

    fun saveCoursesOnDB() {
        viewModelScope.launch(io) {
            val clearDiffered = async { clearDBCoursesUseCase() }
            val clearResult = clearDiffered.await()

            if (clearResult is Resource.Error) {
                _saveCourseOnDBResult.value = clearResult
                return@launch
            }

            _courses.value.data?.let {
                val saveDiffered = async { putDBCoursesUseCase(it) }
                _saveCourseOnDBResult.value = saveDiffered.await()
            }
        }
    }
}