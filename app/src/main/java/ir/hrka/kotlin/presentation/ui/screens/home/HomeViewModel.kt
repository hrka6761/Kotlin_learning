package ir.hrka.kotlin.presentation.ui.screens.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.presentation.GlobalData
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val globalData: GlobalData
): ViewModel() {

    fun openUrl(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}