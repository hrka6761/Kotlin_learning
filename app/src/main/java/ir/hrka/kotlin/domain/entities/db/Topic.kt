package ir.hrka.kotlin.domain.entities.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "topic")
data class Topic(
    @PrimaryKey @SerializedName("topic_id") val id: Int,
    @SerializedName("course") val course: String,
    @SerializedName("topic_title") val topicTitle: String,
    @SerializedName("topic_file_name") val fileName: String,
    @SerializedName("topic_image") val topicImage: String,
    @SerializedName("points_number") val pointsNumber: Int,
    @SerializedName("has_visualizer") val hasVisualizer: Boolean,
    @SerializedName("is_active") val isActive: Boolean,
    @ColumnInfo(name = "has_update") var hasUpdate: Boolean = true,
)
