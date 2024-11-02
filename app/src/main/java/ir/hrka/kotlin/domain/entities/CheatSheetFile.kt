package ir.hrka.kotlin.domain.entities

data class CheatSheetFile(
    val name: String,
    val path: String,
    val sha: String,
    val size: Int,
    val url: String,
    val htmlUrl: String,
    val gitUrl: String,
    val downloadUrl: String,
    val type: String,
    val links: Links,
)
