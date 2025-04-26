package ir.hrka.kotlin.core.utilities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import ir.hrka.kotlin.domain.entities.Point
import androidx.core.net.toUri

private val sendIntent = Intent().apply {
    action = Intent.ACTION_SEND
    type = "text/plain"
}
private val chooserIntent = Intent.createChooser(sendIntent, null)
private val customTabsIntent = CustomTabsIntent.Builder()
    .setShowTitle(true)
    .setUrlBarHidingEnabled(true)
    .build()


fun Activity.sharePoint(point: Point) {
    val shareText = providePointTextForSharing(point)
    sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)

    startActivity(chooserIntent)
}

fun Activity.translatePoint(point: Point) {
    val translationText = providePointTextForTranslate(point)
    val url = "https://translate.google.com/?sl=auto&tl=en&text=${Uri.encode(translationText)}"

    customTabsIntent.launchUrl(this, url.toUri())
}


private fun providePointTextForSharing(point: Point) =
    buildString {
        append(point.headPoint)
        append("\n\n")
        point.subPoints?.forEach { subPoint -> append("* $subPoint\n") }
        append("\n")
        point.snippetsCodes?.forEach { snippetCode -> append("> $snippetCode\n") }
    }

private fun providePointTextForTranslate(point: Point) =
    buildString {
        append(point.headPoint)
        append("\n\n")
        point.snippetsCodes?.forEach { snippetCode -> append("$snippetCode\n") }
    }

