package ir.hrka.kotlin.core.utilities

import android.util.Base64

private const val javaDocPattern = """(?s)/\*\*\s*(.*?)\s*\*/"""
private const val snippetCodePattern = "```(.*?)```"


fun String.decodeBase64(): String {
    val decodedBytes = Base64.decode(this, Base64.DEFAULT)
    return String(decodedBytes)
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

            if (filteredPoint.isNotEmpty())
                filteredPoint.split(".").toMutableList()
            else
                null
        } else
            this.split(":").last().split(".").toMutableList()
    } else
        null

    list?.removeAt(list.size - 1)

    return list
}

fun String.extractFileName(): String = this.split("_").last().split(".").first()

fun String.extractFileIdByName(): Long = this.split("_").first().toLong()

fun String.splitByCapitalLetters(): String = replace(Regex("(?=[A-Z])"), " ")