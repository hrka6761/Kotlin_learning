package ir.hrka.kotlin.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_CODE
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_ID
import ir.hrka.kotlin.core.Constants.FORCE_UPDATE_STATE
import ir.hrka.kotlin.core.Constants.NO_UPDATE_STATE
import ir.hrka.kotlin.core.Constants.UPDATE_STATE
import ir.hrka.kotlin.core.Constants.UPDATE_UNKNOWN_STATE
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Loading
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.ExecutionState.Stop
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.VersionsInfo
import ir.hrka.kotlin.domain.usecases.git.GetChangelogFromGitUseCase
import ir.hrka.kotlin.domain.usecases.preference.GetCoroutineVersionIdUseCase
import ir.hrka.kotlin.domain.usecases.preference.GetCoursesVersionIdUseCase
import ir.hrka.kotlin.domain.usecases.preference.GetKotlinVersionIdUseCase
import ir.hrka.kotlin.presentation.GlobalData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SplashViewModel @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val globalData: GlobalData,
    private val getChangelogFromGitUseCase: GetChangelogFromGitUseCase,
    private val getCoursesVersionIdUseCase: GetCoursesVersionIdUseCase,
    private val getKotlinVersionIdUseCase: GetKotlinVersionIdUseCase,
    private val getCoroutineVersionIdUseCase: GetCoroutineVersionIdUseCase
) : ViewModel() {

    private val _executionState: MutableStateFlow<ExecutionState> = MutableStateFlow(Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _updateState: MutableStateFlow<Int> = MutableStateFlow(UPDATE_UNKNOWN_STATE)
    val updateState: StateFlow<Int> = _updateState
    private val _versionsInfo: MutableStateFlow<Resource<VersionsInfo?>> =
        MutableStateFlow(Resource.Initial())
    private val _coursesVersionId: MutableStateFlow<Resource<Int?>> =
        MutableStateFlow(Resource.Initial())
    private val _kotlinVersionId: MutableStateFlow<Resource<Int?>> =
        MutableStateFlow(Resource.Initial())
    private val _coroutineVersionId: MutableStateFlow<Resource<Int?>> =
        MutableStateFlow(Resource.Initial())


    fun checkChangelog(appVersionCode: Int) {
        if (_executionState.value == Start) {
            setExecutionState(Loading)

            initChangelogResult()
            initCoursesVersionIdResult()
            initKotlinVersionIdResult()
            initCoroutineVersionIdResult(appVersionCode)

            getAppChangelog()
        }
    }


    private fun initChangelogResult() {
        viewModelScope.launch {
            _versionsInfo.collect { result ->
                if (_executionState.value != Stop)
                    when (result) {
                        is Resource.Initial -> {}
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            getCoursesVersionId()
                        }

                        is Resource.Error -> {
                            getCoursesVersionId()
                        }
                    }
            }
        }
    }

    private fun initCoursesVersionIdResult() {
        viewModelScope.launch {
            _coursesVersionId.collect { result ->
                if (_executionState.value != Stop)
                    when (result) {
                        is Resource.Initial -> {}
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            getKotlinVersionId()
                        }

                        is Resource.Error -> {
                            getKotlinVersionId()
                        }
                    }
            }
        }
    }

    private fun initKotlinVersionIdResult() {
        viewModelScope.launch {
            _kotlinVersionId.collect { result ->
                if (_executionState.value != Stop)
                    when (result) {
                        is Resource.Initial -> {}
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            getCoroutineVersionId()
                        }

                        is Resource.Error -> {
                            getCoroutineVersionId()
                        }
                    }
            }
        }
    }

    private fun initCoroutineVersionIdResult(appVersionCode: Int) {
        viewModelScope.launch {
            _coroutineVersionId.collect { result ->
                if (_executionState.value != Stop)
                    when (result) {
                        is Resource.Initial -> {}
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            initGlobalData(appVersionCode)
                            checkNewVersion()
                        }

                        is Resource.Error -> {
                            initGlobalData(appVersionCode)
                            checkNewVersion()
                        }
                    }
            }
        }
    }

    private fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    private fun getAppChangelog() {
        viewModelScope.launch(io) {
            _versionsInfo.value = Resource.Loading()
            _versionsInfo.value = getChangelogFromGitUseCase()
        }
    }

    private fun getCoursesVersionId() {
        viewModelScope.launch(io) {
            _coursesVersionId.value = Resource.Loading()
            _coursesVersionId.value = getCoursesVersionIdUseCase()
        }
    }

    private fun getKotlinVersionId() {
        viewModelScope.launch(io) {
            _kotlinVersionId.value = Resource.Loading()
            _kotlinVersionId.value = getKotlinVersionIdUseCase()
        }
    }

    private fun getCoroutineVersionId() {
        viewModelScope.launch(io) {
            _coroutineVersionId.value = Resource.Loading()
            _coroutineVersionId.value = getCoroutineVersionIdUseCase()
        }
    }

    private fun initGlobalData(appVersionCode: Int) {
        globalData.initGlobalData(
            versionsInfo = _versionsInfo.value.data,
            coursesVersionId = _coursesVersionId.value.data ?: DEFAULT_VERSION_ID,
            kotlinVersionId = _kotlinVersionId.value.data ?: DEFAULT_VERSION_ID,
            coroutineVersionId = _coroutineVersionId.value.data ?: DEFAULT_VERSION_ID,
            appVersionCode = appVersionCode
        )
    }

    private fun checkNewVersion() {
        val appVersionCode = globalData.appVersionCode ?: DEFAULT_VERSION_CODE
        val lastVersionCode = _versionsInfo.value.data?.lastVersionCode ?: appVersionCode
        val minSupportedVersionCode =
            _versionsInfo.value.data?.minSupportedVersionCode ?: appVersionCode

        _updateState.value =
            if (appVersionCode < lastVersionCode)
                if (appVersionCode < minSupportedVersionCode)
                    FORCE_UPDATE_STATE
                else
                    UPDATE_STATE
            else
                NO_UPDATE_STATE
    }
}