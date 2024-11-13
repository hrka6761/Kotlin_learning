package ir.hrka.kotlin.core.utilities

import android.util.Base64
import android.util.Log
import ir.hrka.kotlin.core.utilities.Constants.TAG

private const val versionCodePattern = """versionCode\s*=\s*(\d+)"""
private const val versionNamePattern = """versionName\s*=\s*"([^"]+)""""
private const val versionSuffixPattern = """versionNameSuffix\s*=\s*"([^"]+)""""
private const val javaDocPattern = """(?s)/\*\*\s*(.*?)\s*\*/"""
private const val snippetCodePattern = "```(.*?)```"


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

fun String.extractJavaDocsFromCheatSheetFileContent(): List<String> {
    val javaDocRegex = javaDocPattern.toRegex()

    return javaDocRegex.findAll(this).map { it.groupValues[1].trim() }.toList()
}

fun String.extractRawPointsFromJavaDocContent(): List<String> = this.split("* * ")

fun String.extractClearPointsFromRawPoint(): String =
    this
        .replace("\n", " ")
        .replace("  * ", " ")
        .replace("  ", "")

fun String.extractSnippetCodeFromPoint(): List<String>? {
    val snippetCodeRegex = snippetCodePattern.toRegex(RegexOption.DOT_MATCHES_ALL)

    val snippetsCode =
        snippetCodeRegex.findAll(this).map { it.groupValues[1].trim().replace("*", " ") }.toList()
    var modifiedSnippetsCode: List<String>? = null

    if (snippetsCode.isNotEmpty())
        modifiedSnippetsCode = snippetsCode.map { item -> " $item" }

    return modifiedSnippetsCode
}

fun String.extractHeadPointsFromPointContent(): String = this.split(":").first()

fun String.extractSubPointsFromPointContent(): MutableList<String>? {
    val snippetCodeRegex = snippetCodePattern.toRegex(RegexOption.DOT_MATCHES_ALL)

    val list = if (this.contains(":")) {
        if (this.contains(snippetCodeRegex)) {
            val filteredPoint = this
                .replace(snippetCodeRegex, "")
                .split(":")
                .last()
                .replace("  ", "")
            if (filteredPoint.isEmpty()) {
                filteredPoint.split(".").toMutableList()
            } else
                null
        } else
            this.split(":").last().split(".").toMutableList()
    } else
        null

    list?.removeAt(list.size - 1)

    return list
}

fun String.extractFileName(): String = this.split("_").last().split(".").first()

fun String.extractFileIdByName(): Int = this.split("_").first().toInt()

fun String.splitByCapitalLetters(): String = replace(Regex("(?=[A-Z])"), " ")