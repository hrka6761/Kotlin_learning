package ir.hrka.kotlin.ui.screens.points

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.utilities.Constants.TAG
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.PointDataModel
import ir.hrka.kotlin.domain.entities.db.Point
import ir.hrka.kotlin.domain.entities.db.SnippetCode
import ir.hrka.kotlin.domain.entities.db.SubPoint
import ir.hrka.kotlin.domain.usecases.DeleteCheatSheetPointsUseCase
import ir.hrka.kotlin.domain.usecases.GetDBCheatSheetPointsUseCase
import ir.hrka.kotlin.domain.usecases.GetGithubCheatSheetPointsUseCase
import ir.hrka.kotlin.domain.usecases.SaveCheatsheetPointsOnDBUseCase
import ir.hrka.kotlin.domain.usecases.SavePointSnippetCodesOnDBUseCase
import ir.hrka.kotlin.domain.usecases.SavePointSubPointsOnDBUseCase
import ir.hrka.kotlin.domain.usecases.UpdateCheatSheetUpdateStateUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class PointViewModel @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val getGithubCheatSheetPointsUseCase: GetGithubCheatSheetPointsUseCase,
    private val getDBCheatSheetPointsUseCase: GetDBCheatSheetPointsUseCase,
    private val deleteCheatSheetPointsUseCase: DeleteCheatSheetPointsUseCase,
    private val saveCheatsheetPointsOnDBUseCase: SaveCheatsheetPointsOnDBUseCase,
    private val savePointSubPointsOnDBUseCase: SavePointSubPointsOnDBUseCase,
    private val savePointSnippetCodesOnDBUseCase: SavePointSnippetCodesOnDBUseCase,
    private val updateCheatSheetUpdateStateUseCase: UpdateCheatSheetUpdateStateUseCase
) : ViewModel() {

    private val _points: MutableStateFlow<Resource<List<PointDataModel>?>> =
        MutableStateFlow(Resource.Initial())
    val points: StateFlow<Resource<List<PointDataModel>?>> = _points
    private val _progressBarState: MutableStateFlow<Boolean?> = MutableStateFlow(true)
    val progressBarState: MutableStateFlow<Boolean?> = _progressBarState
    private val _saveCheatsheetPointsResult: MutableStateFlow<Resource<Boolean>> =
        MutableStateFlow(Resource.Initial())
    val saveCheatsheetPointsResult: MutableStateFlow<Resource<Boolean>> =
        _saveCheatsheetPointsResult


    fun setProgressBarState(state: Boolean?) {
        _progressBarState.value = state
    }

    fun getPointsFromGithub(fileName: String) {
        viewModelScope.launch(io) {
            _points.value = Resource.Loading()
            _points.value = getGithubCheatSheetPointsUseCase(fileName)
        }
    }

    fun getPointsFromDatabase(fileName: String) {
        viewModelScope.launch(io) {
            _points.value = Resource.Loading()
            _points.value = getDBCheatSheetPointsUseCase(fileName)
        }
    }

    fun saveCheatsheetsOnDB(cheatsheetName: String) {
        viewModelScope.launch(io) {
            val deleteDiffered = async { deleteCheatSheetPointsUseCase(cheatsheetName) }
            val deleteResult = deleteDiffered.await()

            if (deleteResult is Resource.Error) {
                _saveCheatsheetPointsResult.value = deleteResult
                return@launch
            }

            var savePointResult: Resource<Long>

            _points.value.data?.forEach { pointDataModel ->
                savePointResult = Point(
                    pointText = pointDataModel.headPoint,
                    cheatsheetName = cheatsheetName
                ).let {
                    saveCheatsheetPointsOnDBUseCase(it)
                }

                savePointResult.data?.let { id ->
                    val subPoints = pointDataModel.subPoints?.map { str ->
                        SubPoint(
                            pointId = id,
                            subPointText = str
                        )
                    }
                    subPoints?.let {
                        val result = savePointSubPointsOnDBUseCase(it.toTypedArray())

                        if (result is Resource.Error) {
                            _saveCheatsheetPointsResult.value = result
                            return@forEach
                        }
                    }

                    val snippetCodes = pointDataModel.snippetsCode?.map { str ->
                        SnippetCode(
                            pointId = id,
                            snippetCodeText = str
                        )
                    }
                    snippetCodes?.let {
                        val result = savePointSnippetCodesOnDBUseCase(snippetCodes.toTypedArray())

                        if (result is Resource.Error) {
                            _saveCheatsheetPointsResult.value = result
                            return@forEach
                        }
                    }
                }
            }

            _saveCheatsheetPointsResult.value = Resource.Success(true)
        }
    }

    fun updateCheatsheetState(cheatsheetId: Int) {
        viewModelScope.launch(io) {
            updateCheatSheetUpdateStateUseCase(cheatsheetId, hasContentUpdated = false)
        }
    }
}