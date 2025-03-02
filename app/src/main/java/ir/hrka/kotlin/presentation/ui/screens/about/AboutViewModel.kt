package ir.hrka.kotlin.presentation.ui.screens.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.hrka.kotlin.presentation.GlobalData
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val globalData: GlobalData
) : ViewModel() {

    private val customTabsIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .setUrlBarHidingEnabled(true)
        .build()


    fun openUrl(context: Context, url: String) = customTabsIntent.launchUrl(context, Uri.parse(url))
}