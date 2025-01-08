package ir.hrka.kotlin.domain.entities

import com.google.gson.annotations.SerializedName

data class PointData(
    @SerializedName(value = "topic_id") val topicId: Int,
    @SerializedName(value = "topic_name") val topicName:String,
    @SerializedName(value = "points") val points:List<Point>
)