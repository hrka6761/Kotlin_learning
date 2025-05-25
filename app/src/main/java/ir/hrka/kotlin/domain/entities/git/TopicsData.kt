package ir.hrka.kotlin.domain.entities.git

import com.google.gson.annotations.SerializedName
import ir.hrka.kotlin.domain.entities.db.Topic

data class TopicsData(
    @SerializedName("topics_number") val topicsNumber: Int,
    @SerializedName("topics") val topics: List<Topic>
) : Data<List<Topic>> {

    override fun getMasterData(): List<Topic> = topics
}