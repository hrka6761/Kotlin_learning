package ir.hrka.kotlin.presentation.screens.splash

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.Constants.COROUTINE_VERSION_ID_PREFERENCE_KEY
import ir.hrka.kotlin.core.Constants.COURSES_VERSION_ID_PREFERENCE_KEY
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_CODE
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_ID
import ir.hrka.kotlin.core.Constants.FORCE_UPDATE_STATE
import ir.hrka.kotlin.core.Constants.KOTLIN_VERSION_ID_PREFERENCE_KEY
import ir.hrka.kotlin.core.Constants.NO_UPDATE_STATE
import ir.hrka.kotlin.core.Constants.UPDATE_STATE
import ir.hrka.kotlin.core.Constants.UPDATE_UNKNOWN_STATE
import ir.hrka.kotlin.core.utilities.DataStoreManager
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Loading
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.onError
import ir.hrka.kotlin.core.utilities.onLoading
import ir.hrka.kotlin.core.utilities.onSuccess
import ir.hrka.kotlin.domain.entities.VersionsInfo
import ir.hrka.kotlin.domain.usecases.git.GetChangelogFromGitUseCase
import ir.hrka.kotlin.presentation.GlobalData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SplashViewModel @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val globalData: GlobalData,
    private val getChangelogFromGitUseCase: GetChangelogFromGitUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _executionState: MutableStateFlow<ExecutionState> = MutableStateFlow(Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _updateState: MutableStateFlow<Int> = MutableStateFlow(UPDATE_UNKNOWN_STATE)
    val updateState: StateFlow<Int> = _updateState
    private var _versionsInfo: VersionsInfo? = null
    private var _coursesVersionId: Int = DEFAULT_VERSION_ID
    private var _kotlinVersionId: Int = DEFAULT_VERSION_ID
    private var _coroutineVersionId: Int = DEFAULT_VERSION_ID


    fun checkChangelog(appVersionCode: Int) {
        if (_executionState.value == Start)
            getAppChangelog(appVersionCode)
    }

    private fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    private fun getAppChangelog(appVersionCode: Int) {
        viewModelScope.launch(io) {
            getChangelogFromGitUseCase()
                .collect { result ->
                    result
                        .onLoading {
                            setExecutionState(Loading)
                        }.onSuccess { versionsInfo ->
                            _versionsInfo = versionsInfo
                            initAndCheck(appVersionCode)
                        }.onError {
                            initAndCheck(appVersionCode)
                        }
                }
        }
    }

    private suspend fun getStoredVersionId(preferenceKey: String): Int =
        dataStoreManager.readData(intPreferencesKey(preferenceKey)).first() ?: DEFAULT_VERSION_ID

    private suspend fun initAndCheck(appVersionCode: Int) {
        _coursesVersionId =
            getStoredVersionId(COURSES_VERSION_ID_PREFERENCE_KEY)
        _kotlinVersionId =
            getStoredVersionId(KOTLIN_VERSION_ID_PREFERENCE_KEY)
        _coroutineVersionId =
            getStoredVersionId(COROUTINE_VERSION_ID_PREFERENCE_KEY)

        initGlobalData(appVersionCode)
        checkNewVersion()
    }

    private fun initGlobalData(appVersionCode: Int) {
        globalData.initGlobalData(
            versionsInfo = _versionsInfo,
            coursesVersionId = _coursesVersionId,
            kotlinVersionId = _kotlinVersionId,
            coroutineVersionId = _coroutineVersionId,
            appVersionCode = appVersionCode
        )
    }

    private fun checkNewVersion() {
        val appVersionCode = globalData.appVersionCode ?: DEFAULT_VERSION_CODE
        val lastVersionCode = _versionsInfo?.lastVersionCode ?: appVersionCode
        val minSupportedVersionCode =
            _versionsInfo?.minSupportedVersionCode ?: appVersionCode

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