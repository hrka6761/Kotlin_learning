package ir.hrka.kotlin.ui.screens.coroutine_topics

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
import ir.hrka.kotlin.domain.entities.db.CoroutineTopic
import ir.hrka.kotlin.domain.usecases.db.coroutine.read.GetDBCoroutineTopicsListUseCase
import ir.hrka.kotlin.domain.usecases.db.coroutine.write.ClearCoroutineTopicsTableUseCase
import ir.hrka.kotlin.domain.usecases.db.coroutine.write.SaveCoroutineTopicsOnDBUseCase
import ir.hrka.kotlin.domain.usecases.db.coroutine.write.UpdateCoroutineTopicsStateUseCase
import ir.hrka.kotlin.domain.usecases.git.coroutine.read.GetGitCoroutineTopicsListUseCase
import ir.hrka.kotlin.domain.usecases.preference.LoadCurrentCoroutineCourseVersionNameUseCase
import ir.hrka.kotlin.domain.usecases.preference.SaveCurrentCoroutineCourseVersionNameUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class CoroutineTopicsViewModel @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val getGitCoroutineTopicsListUseCase: GetGitCoroutineTopicsListUseCase,
    private val getDBCoroutineTopicsListUseCase: GetDBCoroutineTopicsListUseCase,
    private val loadCurrentCoroutineCourseVersionNameUseCase: LoadCurrentCoroutineCourseVersionNameUseCase,
    private val saveCurrentCoroutineCourseVersionNameUseCase: SaveCurrentCoroutineCourseVersionNameUseCase,
    private val clearCoroutineTopicsTableUseCase: ClearCoroutineTopicsTableUseCase,
    private val saveCoroutineTopicsOnDBUseCase: SaveCoroutineTopicsOnDBUseCase,
    private val updateCoroutineTopicsStateUseCase: UpdateCoroutineTopicsStateUseCase
) : ViewModel() {

    private val _coroutineTopics: MutableStateFlow<Resource<List<CoroutineTopic>?>> =
        MutableStateFlow(Resource.Initial())
    val coroutineTopics: StateFlow<Resource<List<CoroutineTopic>?>> = _coroutineTopics
    private val _executionState: MutableStateFlow<ExecutionState> = MutableStateFlow(Start)
    val executionState: MutableStateFlow<ExecutionState> = _executionState
    private val _hasUpdateForCoroutineTopicsList: MutableStateFlow<Boolean?> =
        MutableStateFlow(null)
    val hasUpdateForCoroutineTopicsList: StateFlow<Boolean?> = _hasUpdateForCoroutineTopicsList
    private val _hasUpdateForCoroutineTopicsContent: MutableStateFlow<Boolean?> =
        MutableStateFlow(null)
    val hasUpdateForCoroutineTopicsContent: StateFlow<Boolean?> =
        _hasUpdateForCoroutineTopicsContent
    private val _saveCoroutineTopicsListResult: MutableStateFlow<Resource<Boolean>> =
        MutableStateFlow(Resource.Initial())
    val saveCoroutineTopicsListResult: StateFlow<Resource<Boolean>> = _saveCoroutineTopicsListResult
    private val _updateCoroutineTopicsOnDBResult: MutableStateFlow<Resource<Boolean>> =
        MutableStateFlow(Resource.Initial())
    val updateCoroutineTopicsOnDBResult: MutableStateFlow<Resource<Boolean>> =
        _updateCoroutineTopicsOnDBResult
    private lateinit var currentVersionName: String
    private val _failedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val failedState: StateFlow<Boolean> = _failedState


    fun setExecutionState(state: ExecutionState) {
        _executionState.value = state
    }

    fun setFailedState(state: Boolean) {
        _failedState.value = state
    }

    fun getCoroutineTopicsFromGit() {
        viewModelScope.launch(io) {
            _coroutineTopics.value = Resource.Loading()
            _coroutineTopics.value = getGitCoroutineTopicsListUseCase()
        }
    }

    fun getCoroutineTopicsFromDatabase() {
        viewModelScope.launch(io) {
            _coroutineTopics.value = Resource.Loading()
            _coroutineTopics.value = getDBCoroutineTopicsListUseCase()
        }
    }

    fun checkNewUpdateForCoroutineTopicsList(gitVersionName: String?) {
        viewModelScope.launch(io) {
            currentVersionName =
                (loadCurrentCoroutineCourseVersionNameUseCase().data ?: DEFAULT_VERSION_NAME)
                    .ifEmpty { DEFAULT_VERSION_NAME }

            if (gitVersionName.isNullOrEmpty()) {
                _hasUpdateForCoroutineTopicsList.value = false
                return@launch
            }

            val currentVersionMajorDiffered =
                async { currentVersionName.extractMajorFromVersionName() }
            val gitVersionMajorDiffered =
                async { gitVersionName.extractMajorFromVersionName() }

            val gitVersionMajor = gitVersionMajorDiffered.await()
            val currentVersionMajor = currentVersionMajorDiffered.await()

            if (gitVersionMajor != currentVersionMajor) {
                _hasUpdateForCoroutineTopicsList.value = true
                return@launch
            }

            val currentVersionMinorDiffered =
                async { currentVersionName.extractMinorFromVersionName() }
            val gitVersionMinorDiffered =
                async { gitVersionName.extractMinorFromVersionName() }

            val gitVersionMinor = gitVersionMinorDiffered.await()
            val currentVersionMinor = currentVersionMinorDiffered.await()

            if (gitVersionMinor != currentVersionMinor) {
                _hasUpdateForCoroutineTopicsList.value = true
                return@launch
            }

            _hasUpdateForCoroutineTopicsList.value = false
        }
    }

    fun checkNewUpdateForCoroutineTopicsContent(gitVersionName: String?) {
        viewModelScope.launch(io) {
            if (gitVersionName.isNullOrEmpty()) {
                _hasUpdateForCoroutineTopicsContent.value = false
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
                    _hasUpdateForCoroutineTopicsList.value = true
                    return@launch
                }
                _hasUpdateForCoroutineTopicsContent.value = true
                return@launch
            }

            _hasUpdateForCoroutineTopicsContent.value = false
        }
    }

    fun updateCoroutineTopicsInDatabase(gitVersionSuffix: String?) {
        if (gitVersionSuffix.isNullOrEmpty()) {
            _updateCoroutineTopicsOnDBResult.value = Resource.Error(unknownError)
            return
        }

        viewModelScope.launch(io) {
            val updatedKotlinTopicsList =
                gitVersionSuffix.extractUpdatedKotlinTopicsListFromVersionName()

            updateCoroutineTopicsOnDBResult.value = updateCoroutineTopicsStateUseCase(
                *updatedKotlinTopicsList.toIntArray(),
                hasContentUpdated = true
            )
        }
    }

    fun saveCoroutineTopicsOnDB() {
        viewModelScope.launch(io) {
            val clearDiffered = async { clearCoroutineTopicsTableUseCase() }
            val clearResult = clearDiffered.await()

            if (clearResult is Resource.Error) {
                _saveCoroutineTopicsListResult.value = clearResult
                return@launch
            }

            _coroutineTopics.value.data?.let {
                val saveDiffered = async { saveCoroutineTopicsOnDBUseCase(it) }
                _saveCoroutineTopicsListResult.value = saveDiffered.await()
            }
        }
    }

    fun saveVersionName(gitVersionName: String) {
        viewModelScope.launch(io) {
            saveCurrentCoroutineCourseVersionNameUseCase(gitVersionName)
        }
    }

    fun updateCoroutineTopicsList(id: Int) {
        _coroutineTopics.value.data?.get(id)?.hasUpdated = false
    }
}