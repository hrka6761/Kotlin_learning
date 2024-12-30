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
import ir.hrka.kotlin.domain.usecases.git.kotlin.read.GetGitCoursesUseCase
import ir.hrka.kotlin.presentation.GlobalData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val globalData: GlobalData,
    private val getGitCoursesUseCase: GetGitCoursesUseCase
) : ViewModel() {

    private val _coursesList: MutableStateFlow<Resource<List<Course>>> =
        MutableStateFlow(Resource.Initial())
    val coursesList: StateFlow<Resource<List<Course>>> = _coursesList
    private val _executionState: MutableStateFlow<ExecutionState> = MutableStateFlow(Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _failedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val failedState: StateFlow<Boolean> = _failedState


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
        viewModelScope.launch {
            _coursesList.value = Resource.Loading()
            _coursesList.value = getGitCoursesUseCase()
        }
    }
}