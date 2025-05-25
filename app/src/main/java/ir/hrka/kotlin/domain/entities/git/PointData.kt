package ir.hrka.kotlin.domain.entities.git

import com.google.gson.annotations.SerializedName
import ir.hrka.kotlin.domain.entities.git.inner_data.Point

data class PointData(
    @SerializedName(value = "topic_id") val topicId: Int,
    @SerializedName(value = "topic_name") val topicName: String,
    @SerializedName(value = "points") val points: List<Point>
) : Data<List<Point>> {

    override fun getMasterData(): List<Point> = points
}