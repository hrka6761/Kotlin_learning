package ir.hrka.kotlin.domain.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LinksModel(
    @field:Json val self: String,
    @field:Json val git: String,
    @field:Json val html: String
)
