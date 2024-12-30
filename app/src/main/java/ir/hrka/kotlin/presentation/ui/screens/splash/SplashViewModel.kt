package ir.hrka.kotlin.presentation.ui.screens.splash

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.presentation.MainActivity
import ir.hrka.kotlin.core.Constants.BAZAAR_URL
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_ID
import ir.hrka.kotlin.core.Constants.FORCE_UPDATE_STATE
import ir.hrka.kotlin.core.Constants.NO_UPDATE_STATE
import ir.hrka.kotlin.core.Constants.TAG
import ir.hrka.kotlin.core.Constants.UPDATE_STATE
import ir.hrka.kotlin.core.Constants.UPDATE_UNKNOWN_STATE
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.VersionsInfo
import ir.hrka.kotlin.domain.usecases.git.GetAppVersionsUseCase
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
    private val getAppVersionsUseCase: GetAppVersionsUseCase,
    private val getCoursesVersionIdUseCase: GetCoursesVersionIdUseCase,
    private val getKotlinVersionIdUseCase: GetKotlinVersionIdUseCase,
    private val getCoroutineVersionIdUseCase: GetCoroutineVersionIdUseCase
) : ViewModel() {

    private val _versionsInfo: MutableStateFlow<Resource<VersionsInfo>> =
        MutableStateFlow(Resource.Initial())
    val versionsInfo: StateFlow<Resource<VersionsInfo>> = _versionsInfo
    private val _executionState: MutableStateFlow<ExecutionState> = MutableStateFlow(Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _updateState: MutableStateFlow<Int> = MutableStateFlow(UPDATE_UNKNOWN_STATE)
    val updateState: StateFlow<Int> = _updateState
    private val _kotlinLocalVersionId: MutableStateFlow<Resource<Int?>> =
        MutableStateFlow(Resource.Initial())
    val kotlinLocalVersionId: StateFlow<Resource<Int?>> = _kotlinLocalVersionId
    private val _coursesLocalVersionId: MutableStateFlow<Resource<Int?>> =
        MutableStateFlow(Resource.Initial())
    val coursesLocalVersionId: StateFlow<Resource<Int?>> = _coursesLocalVersionId
    private val _coroutineLocalVersionId: MutableStateFlow<Resource<Int?>> =
        MutableStateFlow(Resource.Initial())
    val coroutineLocalVersionId: StateFlow<Resource<Int?>> = _coroutineLocalVersionId


    fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    fun setNewVersionDialogState(state: Int) {
        _updateState.value = state
    }

    fun getAppVersions() {
        viewModelScope.launch(io) {
            _versionsInfo.value = Resource.Loading()
            _versionsInfo.value = getAppVersionsUseCase()
        }
    }

    fun checkNewVersion(context: Context) {
        val appVersionCode = getAppVersionCode(context)
        val lastVersionCode = _versionsInfo.value.data?.lastVersionCode ?: appVersionCode
        val minSupportedVersionId = _versionsInfo.value.data?.minSupportedVersionCode
            ?: DEFAULT_VERSION_ID

        _updateState.value =
            if (appVersionCode != lastVersionCode)
                if (appVersionCode < minSupportedVersionId)
                    FORCE_UPDATE_STATE
                else
                    UPDATE_STATE
            else
                NO_UPDATE_STATE
    }

    fun getCoursesVersionId() {
        viewModelScope.launch(io) {
            _coursesLocalVersionId.value = Resource.Loading()
            _coursesLocalVersionId.value = getKotlinVersionIdUseCase()
        }
    }

    fun getKotlinVersionId() {
        viewModelScope.launch(io) {
            _kotlinLocalVersionId.value = Resource.Loading()
            _kotlinLocalVersionId.value = getKotlinVersionIdUseCase()
        }
    }

    fun getCoroutineVersionId() {
        viewModelScope.launch(io) {
            _coroutineLocalVersionId.value = Resource.Loading()
            _coroutineLocalVersionId.value = getCoroutineVersionIdUseCase()
        }
    }

    fun initGlobalData() {
        _versionsInfo.value.data?.let { versionsInfo ->
            globalData.initGlobalData(
                versionsInfo = versionsInfo,
                lastVersionId = _versionsInfo.value.data?.lastVersionId,
                coursesVersionId = _coursesLocalVersionId.value.data ?: DEFAULT_VERSION_ID,
                kotlinVersionId = _kotlinLocalVersionId.value.data ?: DEFAULT_VERSION_ID,
                coroutineVersionId = _coroutineLocalVersionId.value.data ?: DEFAULT_VERSION_ID
            )
        }
    }

    fun goToUpdate(activity: MainActivity) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(BAZAAR_URL))
        activity.startActivity(intent)
    }


    private fun getAppVersionCode(context: Context): Int {
        val packageInfo: PackageInfo =
            context.packageManager.getPackageInfo(context.packageName, 0)
        val versionCode = packageInfo.longVersionCode.toInt()

        return versionCode
    }
}