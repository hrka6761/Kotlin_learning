package ir.hrka.kotlin.domain.entities.git

import com.google.gson.annotations.SerializedName
import ir.hrka.kotlin.domain.entities.git.inner_data.Version

data class VersionsData(
    @SerializedName("last_version_id") val lastVersionId: Int,
    @SerializedName("last_version_code") val lastVersionCode: Int,
    @SerializedName("min_supported_version_code") val minSupportedVersionCode: Int,
    @SerializedName("versions") val versions: List<Version>
) : Data<VersionsData> {

    override fun getMasterData(): VersionsData = this
}