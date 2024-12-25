package ir.hrka.kotlin.presentation.ui.screens.kotlin_topics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_NAME
import ir.hrka.kotlin.core.errors.unknownError
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.string_utilities.extractMajorFromVersionName
import ir.hrka.kotlin.core.utilities.string_utilities.extractMinorFromVersionName
import ir.hrka.kotlin.core.utilities.string_utilities.extractPatchFromVersionName
import ir.hrka.kotlin.core.utilities.string_utilities.extractUpdatedKotlinTopicsListFromVersionName
import ir.hrka.kotlin.domain.entities.db.KotlinTopic
import ir.hrka.kotlin.domain.usecases.db.kotlin.write.ClearKotlinTopicsTableUseCase
import ir.hrka.kotlin.domain.usecases.db.kotlin.read.GetDBKotlinTopicsListUseCase
import ir.hrka.kotlin.domain.usecases.git.kotlin.read.GetGitKotlinTopicsListUseCase
import ir.hrka.kotlin.domain.usecases.preference.LoadCurrentKotlinCourseVersionNameUseCase
import ir.hrka.kotlin.domain.usecases.db.kotlin.write.SaveKotlinTopicsOnDBUseCase
import ir.hrka.kotlin.domain.usecases.preference.SaveCurrentKotlinCourseVersionNameUseCase
import ir.hrka.kotlin.domain.usecases.db.kotlin.write.UpdateKotlinTopicsStateUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class KotlinTopicsViewModel @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val getGitKotlinTopicsListUseCase: GetGitKotlinTopicsListUseCase,
    private val getDBKotlinTopicsListUseCase: GetDBKotlinTopicsListUseCase,
    private val loadCurrentKotlinCourseVersionNameUseCase: LoadCurrentKotlinCourseVersionNameUseCase,
    private val saveCurrentKotlinCourseVersionNameUseCase: SaveCurrentKotlinCourseVersionNameUseCase,
    private val clearKotlinTopicsTableUseCase: ClearKotlinTopicsTableUseCase,
    private val saveKotlinTopicsOnDBUseCase: SaveKotlinTopicsOnDBUseCase,
    private val updateKotlinTopicsStateUseCase: UpdateKotlinTopicsStateUseCase
) : ViewModel() {

    private val _kotlinTopics: MutableStateFlow<Resource<List<KotlinTopic>?>> =
        MutableStateFlow(Resource.Initial())
    val kotlinTopics: StateFlow<Resource<List<KotlinTopic>?>> = _kotlinTopics
    private val _executionState: MutableStateFlow<ExecutionState> = MutableStateFlow(Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _hasUpdateForKotlinTopicsList: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val hasUpdateForKotlinTopicsList: StateFlow<Boolean?> = _hasUpdateForKotlinTopicsList
    private val _hasUpdateForKotlinTopicsContent: MutableStateFlow<Boolean?> =
        MutableStateFlow(null)
    val hasUpdateForKotlinTopicsContent: StateFlow<Boolean?> = _hasUpdateForKotlinTopicsContent
    private val _saveKotlinTopicsListResult: MutableStateFlow<Resource<Boolean>> =
        MutableStateFlow(Resource.Initial())
    val saveKotlinTopicsListResult: StateFlow<Resource<Boolean>> = _saveKotlinTopicsListResult
    private val _updateKotlinTopicsOnDBResult: MutableStateFlow<Resource<Boolean>> =
        MutableStateFlow(Resource.Initial())
    val updateKotlinTopicsOnDBResult: MutableStateFlow<Resource<Boolean>> =
        _updateKotlinTopicsOnDBResult
    private lateinit var currentVersionName: String
    private val _failedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val failedState: StateFlow<Boolean> = _failedState


    fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    fun setFailedState(state: Boolean) {
        _failedState.value = state
    }

    fun getKotlinTopicsFromGit() {
        viewModelScope.launch(io) {
            _kotlinTopics.value = Resource.Loading()
            _kotlinTopics.value = getGitKotlinTopicsListUseCase()
        }
    }

    fun getKotlinTopicsFromDatabase() {
        viewModelScope.launch(io) {
            _kotlinTopics.value = Resource.Loading()
            _kotlinTopics.value = getDBKotlinTopicsListUseCase()
        }
    }

    fun checkNewUpdateForKotlinTopicsList(gitVersionName: String?) {
        viewModelScope.launch(io) {
            currentVersionName =
                (loadCurrentKotlinCourseVersionNameUseCase().data ?: DEFAULT_VERSION_NAME)
                    .ifEmpty { DEFAULT_VERSION_NAME }

            if (gitVersionName.isNullOrEmpty()) {
                _hasUpdateForKotlinTopicsList.value = false
                return@launch
            }

            val currentVersionMajorDiffered =
                async { currentVersionName.extractMajorFromVersionName() }
            val gitVersionMajorDiffered =
                async { gitVersionName.extractMajorFromVersionName() }

            val gitVersionMajor = gitVersionMajorDiffered.await()
            val currentVersionMajor = currentVersionMajorDiffered.await()

            if (gitVersionMajor != currentVersionMajor) {
                _hasUpdateForKotlinTopicsList.value = true
                return@launch
            }

            val currentVersionMinorDiffered =
                async { currentVersionName.extractMinorFromVersionName() }
            val gitVersionMinorDiffered =
                async { gitVersionName.extractMinorFromVersionName() }

            val gitVersionMinor = gitVersionMinorDiffered.await()
            val currentVersionMinor = currentVersionMinorDiffered.await()

            if (gitVersionMinor != currentVersionMinor) {
                _hasUpdateForKotlinTopicsList.value = true
                return@launch
            }

            _hasUpdateForKotlinTopicsList.value = false
        }
    }

    fun checkNewUpdateForKotlinTopicsContent(gitVersionName: String?) {
        viewModelScope.launch(io) {
            if (gitVersionName.isNullOrEmpty()) {
                _hasUpdateForKotlinTopicsContent.value = false
                return@launch
            }

            val currentVersionPatchDiffered =
                async { currentVersionName.extractPatchFromVersionName() }
            val gitVersionPatchDiffered =
                async { gitVersionName.extractPatchFromVersionName() }

            val gitVersionPatch = gitVersionPatchDiffered.await()
            val currentVersionPatch = currentVersionPatchDiffered.await()

            if (gitVersionPatch != currentVersionPatch) {
                if ((gitVersionPatch - currentVersionPatch) > 1) {
                    _hasUpdateForKotlinTopicsList.value = true
                    return@launch
                }
                _hasUpdateForKotlinTopicsContent.value = true
                return@launch
            }

            _hasUpdateForKotlinTopicsContent.value = false
        }
    }

    fun updateKotlinTopicsInDatabase(gitVersionSuffix: String?) {
        if (gitVersionSuffix.isNullOrEmpty()) {
            _updateKotlinTopicsOnDBResult.value = Resource.Error(unknownError)
            return
        }

        viewModelScope.launch(io) {
            val updatedKotlinTopicsList =
                gitVersionSuffix.extractUpdatedKotlinTopicsListFromVersionName()

            updateKotlinTopicsOnDBResult.value = updateKotlinTopicsStateUseCase(
                *updatedKotlinTopicsList.toIntArray(),
                hasContentUpdated = true
            )
        }
    }

    fun saveKotlinTopicsOnDB() {
        viewModelScope.launch(io) {
            val clearDiffered = async { clearKotlinTopicsTableUseCase() }
            val clearResult = clearDiffered.await()

            if (clearResult is Resource.Error) {
                _saveKotlinTopicsListResult.value = clearResult
                return@launch
            }

            _kotlinTopics.value.data?.let {
                val saveDiffered = async { saveKotlinTopicsOnDBUseCase(it) }
                _saveKotlinTopicsListResult.value = saveDiffered.await()
            }
        }
    }

    fun saveVersionName(gitVersionName: String) {
        viewModelScope.launch(io) {
            saveCurrentKotlinCourseVersionNameUseCase(gitVersionName)
        }
    }

    fun updateKotlinTopicsList(id: Int) {
        _kotlinTopics.value.data?.get(id)?.hasUpdated = false
    }
}