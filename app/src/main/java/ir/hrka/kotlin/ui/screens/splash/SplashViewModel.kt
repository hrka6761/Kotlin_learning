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
import ir.hrka.kotlin.core.Constants.NEW_VERSION_NOT_AVAILABLE
import ir.hrka.kotlin.core.Constants.NEW_VERSION_NO_STATE
import ir.hrka.kotlin.core.Constants.NEW_VERSION_AVAILABLE
import ir.hrka.kotlin.core.Constants.NEW_VERSION_UNKNOWN_STATE
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.AppInfo
import ir.hrka.kotlin.domain.usecases.git.GetAppInfoUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SplashViewModel @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val getAppInfoUseCase: GetAppInfoUseCase,
) : ViewModel() {

    private val _appInfo: MutableStateFlow<Resource<AppInfo?>> =
        MutableStateFlow(Resource.Initial())
    val appInfo: StateFlow<Resource<AppInfo?>> = _appInfo
    private val _executionState: MutableStateFlow<ExecutionState> = MutableStateFlow(Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _newVersionState: MutableStateFlow<Int> =
        MutableStateFlow(NEW_VERSION_NO_STATE)
    val newVersionState: StateFlow<Int> = _newVersionState


    fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    fun setNewVersionDialogState(state: Int) {
        _newVersionState.value = state
    }

    fun getAppInfo() {
        viewModelScope.launch(io) {
            _appInfo.value = Resource.Loading()
            _appInfo.value = getAppInfoUseCase()
        }
    }

    fun checkNewVersion(context: Context) {
        try {
            val packageInfo: PackageInfo =
                context.packageManager.getPackageInfo(context.packageName, 0)
            val versionCode = packageInfo.longVersionCode.toInt()
            if (_appInfo.value.data != null)
                if (versionCode < _appInfo.value.data!!.versionCode)
                    _newVersionState.value = NEW_VERSION_AVAILABLE
                else
                    _newVersionState.value = NEW_VERSION_NOT_AVAILABLE
        } catch (e: Exception) {
            _newVersionState.value = NEW_VERSION_UNKNOWN_STATE
        }
    }

    fun goToUpdate(activity: MainActivity) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(BAZAAR_URL))
        activity.startActivity(intent)
    }
}