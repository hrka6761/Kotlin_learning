package ir.hrka.kotlin.domain.entities.git.inner_data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Links(
    @field:Json val self: String,
    @field:Json val git: String,
    @field:Json val html: String
)
