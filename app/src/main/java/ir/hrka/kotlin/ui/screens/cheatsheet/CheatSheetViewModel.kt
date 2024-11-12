package ir.hrka.kotlin.ui.screens.cheatsheet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.utilities.Constants.TAG
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.PointDataModel
import ir.hrka.kotlin.domain.usecases.GetCheatSheetContent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class CheatSheetViewModel @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val getCheatSheetContent: GetCheatSheetContent
) : ViewModel() {

    private val _points: MutableStateFlow<Resource<List<PointDataModel>?>> =
        MutableStateFlow(Resource.Initial())
    val points: StateFlow<Resource<List<PointDataModel>?>> = _points


    fun getPoints(fileName: String) {
        viewModelScope.launch(io) {
            _points.value = Resource.Loading()
            _points.value = getCheatSheetContent(fileName)
        }
    }
}