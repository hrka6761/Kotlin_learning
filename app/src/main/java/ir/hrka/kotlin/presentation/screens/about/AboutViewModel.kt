package ir.hrka.kotlin.presentation.screens.about

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.presentation.GlobalData
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(private val globalData: GlobalData) : ViewModel()