package ir.hrka.kotlin.core.utilities

import android.util.Base64

private const val capitalLetterPattern = "(?=[A-Z])"
private const val visualizedPattern = "\\(.*?\\)"


fun String.decodeBase64(): String {
    val decodedBytes = Base64.decode(this, Base64.DEFAULT)
    return String(decodedBytes)
}

fun String.extractFileName(): String = this.split("_").last().split(".").first()

fun String.extractFileIdByName(): Long = this.split("_").first().toLong()

fun String.splitByCapitalLetters(): String = replace(capitalLetterPattern.toRegex(), " ")

fun String.removeVisualizedFromFileName(): String = this.replace(visualizedPattern.toRegex(), "")

fun String.isTopicVisualized(): Boolean = this.contains("visualized")