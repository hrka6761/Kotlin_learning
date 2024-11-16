package ir.hrka.kotlin.ui.screens.cheatsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.PointDataModel
import ir.hrka.kotlin.domain.usecases.GetCheatSheetPoints
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class PointViewModel @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val getCheatSheetPoints: GetCheatSheetPoints
) : ViewModel() {

    private val _points: MutableStateFlow<Resource<List<PointDataModel>?>> =
        MutableStateFlow(Resource.Initial())
    val points: StateFlow<Resource<List<PointDataModel>?>> = _points
    private val _progressBarState: MutableStateFlow<Boolean?> = MutableStateFlow(true)
    val progressBarState: MutableStateFlow<Boolean?> = _progressBarState


    fun setProgressBarState(state: Boolean?) {
        _progressBarState.value = state
    }

    fun getPoints(fileName: String) {
        viewModelScope.launch(io) {
            _points.value = Resource.Loading()
            _points.value = getCheatSheetPoints(fileName)
        }
    }
}