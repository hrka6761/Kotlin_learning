package ir.hrka.kotlin.core.utilities

import android.util.Base64

private const val versionCodePattern = """versionCode\s*=\s*(\d+)"""
private const val versionNamePattern = """versionName\s*=\s*"([^"]+)""""
private const val versionSuffixPattern = """versionNameSuffix\s*=\s*"([^"]+)""""


fun String.decodeBase64(): String {
    val decodedBytes = Base64.decode(this, Base64.DEFAULT)
    return String(decodedBytes)
}

fun String.extractVersionCodeFromGradleContent(): Int {
    val versionCodeRegex = versionCodePattern.toRegex()

    return (versionCodeRegex.find(this)?.groups?.get(1)?.value ?: "-1").toInt()
}

fun String.extractVersionNameFromGradleContent(): String {
    val versionNameRegex = versionNamePattern.toRegex()

    return versionNameRegex.find(this)?.groups?.get(1)?.value ?: "Unknown"
}

fun String.extractVersionSuffixFromGradleContent(): String {
    val versionNameSuffixRegex = versionSuffixPattern.toRegex()

    return versionNameSuffixRegex.find(this)?.groups?.get(1)?.value ?: "Unknown"
}

fun String.extractFileName(): String = this.split("_").last().split(".").first()

fun String.extractFileIdByName(): Int = this.split("_").first().toInt()

fun String.splitByCapitalLetters(): String = replace(Regex("(?=[A-Z])"), " ")