package ir.hrka.kotlin.domain.entities

import com.google.gson.annotations.SerializedName

data class Version(
    @SerializedName("version_id") val versionId: Int,
    @SerializedName("version_name") val versionName: String,
    @SerializedName("version_code") val versionCode: Int,
    @SerializedName("version_type") val versionType: String,
    @SerializedName("updated_coroutine_topics") val updatedCoroutineTopics: String?,
    @SerializedName("updated_kotlin_topics") val updatedKotlinTopics: String?,
    @SerializedName("change_log") val changeLog: List<String>
)