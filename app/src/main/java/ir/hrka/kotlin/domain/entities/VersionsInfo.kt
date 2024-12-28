package ir.hrka.kotlin.domain.entities

import com.google.gson.annotations.SerializedName

data class VersionsInfo(
    @SerializedName("last_version_id") val lastVersionId: Int,
    @SerializedName("last_version_code") val lastVersionCode: Int,
    @SerializedName("min_supported_version_code") val minSupportedVersionCode: Int,
    @SerializedName("versions") val versions: List<Version>
)