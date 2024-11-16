package ir.hrka.kotlin.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.extractMajorFromVersionName
import ir.hrka.kotlin.core.utilities.extractMinorFromVersionName
import ir.hrka.kotlin.domain.entities.RepoFileModel
import ir.hrka.kotlin.domain.entities.db.Cheatsheet
import ir.hrka.kotlin.domain.usecases.ClearCheatsheetsOnDBUseCase
import ir.hrka.kotlin.domain.usecases.GetDBCheatsheetsUseCase
import ir.hrka.kotlin.domain.usecases.GetGithubCheatSheetsUseCase
import ir.hrka.kotlin.domain.usecases.LoadCurrentVersionNameUseCase
import ir.hrka.kotlin.domain.usecases.SaveCheatSheetsOnDBUseCase
import ir.hrka.kotlin.domain.usecases.SaveCurrentVersionNameUseCase
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
    private val getGithubCheatSheetsUseCase: GetGithubCheatSheetsUseCase,
    private val getDBCheatsheetsUseCase: GetDBCheatsheetsUseCase,
    private val loadCurrentVersionNameUseCase: LoadCurrentVersionNameUseCase,
    private val saveCurrentVersionNameUseCase: SaveCurrentVersionNameUseCase,
    private val clearCheatsheetsOnDBUseCase: ClearCheatsheetsOnDBUseCase,
    private val saveCheatsheetsOnDBUseCase: SaveCheatSheetsOnDBUseCase
) : ViewModel() {

    private val _cheatSheets: MutableStateFlow<Resource<List<RepoFileModel>?>> =
        MutableStateFlow(Resource.Initial())
    val cheatSheets: StateFlow<Resource<List<RepoFileModel>?>> = _cheatSheets
    private val _progressBarState: MutableStateFlow<Boolean?> = MutableStateFlow(true)
    val progressBarState: MutableStateFlow<Boolean?> = _progressBarState
    private val _hasNewUpdate: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val hasNewUpdate: StateFlow<Boolean?> = _hasNewUpdate
    private val _savePointsResult: MutableStateFlow<Resource<Boolean>> =
        MutableStateFlow(Resource.Initial())
    val savePointsResult: StateFlow<Resource<Boolean>> = _savePointsResult


    fun setProgressBarState(state: Boolean?) {
        _progressBarState.value = state
    }

    fun getCheatSheetsFromGithub() {
        viewModelScope.launch(io) {
            _cheatSheets.value = Resource.Loading()
            _cheatSheets.value = getGithubCheatSheetsUseCase()
        }
    }

    fun getCheatSheetFromDatabase() {
        viewModelScope.launch(io) {
            _cheatSheets.value = Resource.Loading()
            _cheatSheets.value = getDBCheatsheetsUseCase()
        }
    }

    fun checkNewUpdateForCheatsheetsList(githubVersionName: String?) {
        viewModelScope.launch(io) {
            var currentVersionName = loadCurrentVersionNameUseCase().data

            if (currentVersionName.isNullOrEmpty())
                currentVersionName = "0.0.0"

            if (githubVersionName.isNullOrEmpty()) {
                _hasNewUpdate.value = false
                return@launch
            }

            val currentVersionMajorDiffered =
                async { currentVersionName.extractMajorFromVersionName() }
            val githubVersionMajorDiffered =
                async { githubVersionName.extractMajorFromVersionName() }

            val githubVersionMajor = githubVersionMajorDiffered.await()
            val currentVersionMajor = currentVersionMajorDiffered.await() ?: -1

            if (githubVersionMajor != currentVersionMajor){
                _hasNewUpdate.value = true
                return@launch
            }

            val currentVersionMinorDiffered =
                async { currentVersionName.extractMinorFromVersionName() }
            val githubVersionMinorDiffered =
                async { githubVersionName.extractMinorFromVersionName() }

            val githubVersionMinor = githubVersionMinorDiffered.await()
            val currentVersionMinor = currentVersionMinorDiffered.await() ?: -1

            if (githubVersionMinor != currentVersionMinor){
                _hasNewUpdate.value = true
                return@launch
            }

            _hasNewUpdate.value = false
        }
    }

    fun saveCheatsheetsOnDB(githubVersionName: String) {
        viewModelScope.launch(io) {
            val clearDiffered = async { clearCheatsheetsOnDBUseCase() }
            val clearResult = clearDiffered.await()
            if (clearResult is Resource.Error) {
                _savePointsResult.value = clearResult
                return@launch
            }
            clearCheatsheetsOnDBUseCase()
            _cheatSheets.value.data?.map { repoFileModel ->
                Cheatsheet(
                    id = repoFileModel.id,
                    title = repoFileModel.name,
                    versionName = githubVersionName
                )
            }?.let {
                val saveDiffered = async { saveCheatsheetsOnDBUseCase(it) }
                _savePointsResult.value = saveDiffered.await()
            }
        }
    }

    fun saveVersionMinor(githubVersionName: String) {
        viewModelScope.launch(io) {
            saveCurrentVersionNameUseCase(githubVersionName)
        }
    }
}