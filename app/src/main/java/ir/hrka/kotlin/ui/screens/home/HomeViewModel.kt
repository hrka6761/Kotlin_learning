package ir.hrka.kotlin.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.core.utilities.Constants.TAG
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.RepoFileModel
import ir.hrka.kotlin.domain.usecases.GetCheatSheetsListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val getCheatSheetsListUseCase: GetCheatSheetsListUseCase,
) : ViewModel() {

    private val _cheatSheets: MutableStateFlow<Resource<List<RepoFileModel>?>> =
        MutableStateFlow(Resource.Initial())
    val cheatSheets: StateFlow<Resource<List<RepoFileModel>?>> = _cheatSheets
    private val _progressBarState: MutableStateFlow<Boolean?> = MutableStateFlow(true)
    val progressBarState: MutableStateFlow<Boolean?> = _progressBarState


    fun setProgressBarState(state: Boolean?) {
        _progressBarState.value = state
    }

    fun getCheatSheets() {
        viewModelScope.launch(io) {
            _cheatSheets.value = Resource.Loading()
            _cheatSheets.value = getCheatSheetsListUseCase()
        }
    }
}