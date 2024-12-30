package ir.hrka.kotlin.domain.entities.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "course")
data class Course(
    @PrimaryKey (autoGenerate = true) val courseDBId: Int,
    @SerializedName("course_id") val courseId: Int,
    @SerializedName("course_title") val courseTitle: String,
    @SerializedName("course_description") val courseDesc: String,
    @SerializedName("course_banner") val courseBanner: String,
    @SerializedName("is_active") val isActive: Boolean,
)
