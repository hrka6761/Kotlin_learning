package ir.hrka.kotlin.ui.screens.splash

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.MainActivity
import ir.hrka.kotlin.core.Constants.BAZAAR_URL
import ir.hrka.kotlin.core.Constants.FORCE_UPDATE_STATE
import ir.hrka.kotlin.core.Constants.NO_UPDATE_STATE
import ir.hrka.kotlin.core.Constants.UPDATE_STATE
import ir.hrka.kotlin.core.Constants.UPDATE_UNKNOWN_STATE
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.VersionsInfo
import ir.hrka.kotlin.domain.usecases.git.GetAppVersionsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SplashViewModel @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val getAppVersionsUseCase: GetAppVersionsUseCase,
) : ViewModel() {

    private val _versionsInfo: MutableStateFlow<Resource<VersionsInfo>> =
        MutableStateFlow(Resource.Initial())
    val versionsInfo: StateFlow<Resource<VersionsInfo>> = _versionsInfo
    private val _executionState: MutableStateFlow<ExecutionState> = MutableStateFlow(Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _updateState: MutableStateFlow<Int> = MutableStateFlow(UPDATE_UNKNOWN_STATE)
    val updateState: StateFlow<Int> = _updateState


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
        val currentVersionId = getAppVersionCode(context)
        val lastVersionId = _versionsInfo.value.data?.lastVersionCode
        val minSupportedVersionId = _versionsInfo.value.data?.minSupportedVersionCode ?: 1

        _updateState.value =
            if (currentVersionId != lastVersionId)
                if (currentVersionId < minSupportedVersionId)
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