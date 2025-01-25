package ir.hrka.kotlin.domain.entities.db

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import ir.hrka.kotlin.core.utilities.TopicDeserializer

@Parcelize
@JsonAdapter(TopicDeserializer::class)
@Entity(tableName = "topic")
data class Topic(
    @PrimaryKey @ColumnInfo("topic_id") @SerializedName(value = "topic_id") val id: Int,
    @ColumnInfo(name = "has_update") var hasUpdate: Boolean,
    @ColumnInfo(name = "course_name") @SerializedName(value = "course_name") val courseName: String,
    @ColumnInfo(name = "topic_title") @SerializedName(value = "topic_title") val topicTitle: String,
    @ColumnInfo(name = "topic_file_name") @SerializedName(value = "topic_file_name") val fileName: String,
    @ColumnInfo(name = "has_visualizer") @SerializedName(value = "has_visualizer") val hasVisualizer: Boolean,
    @ColumnInfo(name = "visualizer_version_code") @SerializedName(value = "visualizer_version_code") val visualizerVersionCode: Int,
    @ColumnInfo(name = "visualizer_destination") @SerializedName(value = "visualizer_destination") val visualizerDestination: String,
    @ColumnInfo(name = "is_active") @SerializedName(value = "is_active") val isActive: Boolean,
) : Parcelable
