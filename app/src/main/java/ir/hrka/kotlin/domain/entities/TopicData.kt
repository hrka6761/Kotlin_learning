package ir.hrka.kotlin.domain.entities

import com.google.gson.annotations.SerializedName
import ir.hrka.kotlin.domain.entities.db.Topic

data class TopicData(
    @SerializedName("topics_number") val topicsNumber: Int,
    @SerializedName("topics") val topics: List<Topic>
)