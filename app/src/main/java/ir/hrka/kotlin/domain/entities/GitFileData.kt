package ir.hrka.kotlin.domain.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GitFileData(
    @field:Json val name: String,
    @field:Json val path: String,
    @field:Json val sha: String,
    @field:Json val size: Int,
    @field:Json val url: String,
    @field:Json(name = "html_url") val htmlUrl: String,
    @field:Json(name = "git_url") val gitUrl: String,
    @field:Json(name = "download_url") val downloadUrl: String,
    @field:Json val type: String,
    @field:Json val content: String?,
    @field:Json val encoding: String?,
    @field:Json(name = "_links") val links: Links?,
)
