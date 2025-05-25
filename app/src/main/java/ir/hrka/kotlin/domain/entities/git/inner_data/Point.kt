package ir.hrka.kotlin.domain.entities.git.inner_data

import com.google.gson.annotations.SerializedName

data class Point(
    @SerializedName(value = "id") val id: Long,
    @SerializedName(value = "point") val headPoint: String,
    @SerializedName(value = "sub_points") val subPoints: List<String>?,
    @SerializedName(value = "snippet_codes") val snippetsCodes: List<String>?
)
