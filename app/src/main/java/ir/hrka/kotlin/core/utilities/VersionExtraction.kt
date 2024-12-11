package ir.hrka.kotlin.core.utilities

private const val versionCodePattern = """versionCode\s*=\s*(\d+)"""
private const val versionNamePattern = """versionName\s*=\s*"([^"]+)""""
private const val versionSuffixPattern = """versionNameSuffix\s*=\s*"([^"]+)""""


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

fun String.extractPatchFromVersionName(): Int = this.split(".")[2].toInt()

fun String.extractMinorFromVersionName(): Int = this.split(".")[1].toInt()

fun String.extractMajorFromVersionName(): Int = this.split(".")[0].toInt()

fun String.extractUpdatedKotlinTopicsListFromVersionName(): List<Int> =
    this.split("_").map { stringId -> stringId.toInt() }