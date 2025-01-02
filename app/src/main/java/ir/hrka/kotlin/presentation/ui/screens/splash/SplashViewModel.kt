package ir.hrka.kotlin.presentation.ui.screens.splash

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.presentation.MainActivity
import ir.hrka.kotlin.core.Constants.BAZAAR_URL
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_ID
import ir.hrka.kotlin.core.Constants.FORCE_UPDATE_STATE
import ir.hrka.kotlin.core.Constants.NO_UPDATE_STATE
import ir.hrka.kotlin.core.Constants.UPDATE_STATE
import ir.hrka.kotlin.core.Constants.UPDATE_UNKNOWN_STATE
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.VersionsInfo
import ir.hrka.kotlin.domain.usecases.git.GetChangelogUseCase
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
    private val getChangelogUseCase: GetChangelogUseCase,
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
    val changelog: StateFlow<Resource<VersionsInfo?>> = _versionsInfo
    private val _coursesVersionId: MutableStateFlow<Resource<Int?>> =
        MutableStateFlow(Resource.Initial())
    val coursesVersionId: StateFlow<Resource<Int?>> = _coursesVersionId
    private val _kotlinVersionId: MutableStateFlow<Resource<Int?>> =
        MutableStateFlow(Resource.Initial())
    val kotlinVersionId: StateFlow<Resource<Int?>> = _kotlinVersionId
    private val _coroutineVersionId: MutableStateFlow<Resource<Int?>> =
        MutableStateFlow(Resource.Initial())
    val coroutineVersionId: StateFlow<Resource<Int?>> = _coroutineVersionId


    fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    fun getAppChangelog() {
        viewModelScope.launch(io) {
            _versionsInfo.value = Resource.Loading()
            _versionsInfo.value = getChangelogUseCase()
        }
    }

    fun getCoursesVersionId() {
        viewModelScope.launch(io) {
            _coursesVersionId.value = Resource.Loading()
            _coursesVersionId.value = getCoursesVersionIdUseCase()
        }
    }

    fun getKotlinVersionId() {
        viewModelScope.launch(io) {
            _kotlinVersionId.value = Resource.Loading()
            _kotlinVersionId.value = getKotlinVersionIdUseCase()
        }
    }

    fun getCoroutineVersionId() {
        viewModelScope.launch(io) {
            _coroutineVersionId.value = Resource.Loading()
            _coroutineVersionId.value = getCoroutineVersionIdUseCase()
        }
    }

    fun initGlobalData() {
        globalData.initGlobalData(
            versionsInfo = _versionsInfo.value.data,
            coursesVersionId = _coursesVersionId.value.data ?: DEFAULT_VERSION_ID,
            kotlinVersionId = _kotlinVersionId.value.data ?: DEFAULT_VERSION_ID,
            coroutineVersionId = _coroutineVersionId.value.data ?: DEFAULT_VERSION_ID
        )
    }

    fun checkNewVersion(context: Context) {
        val appVersionCode = getAppVersionCode(context)
        val lastVersionCode = _versionsInfo.value.data?.lastVersionCode ?: appVersionCode
        val minSupportedVersionCode =
            _versionsInfo.value.data?.minSupportedVersionCode ?: appVersionCode

        _updateState.value =
            if (appVersionCode != lastVersionCode)
                if (appVersionCode < minSupportedVersionCode)
                    FORCE_UPDATE_STATE
                else
                    UPDATE_STATE
            else
                NO_UPDATE_STATE
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