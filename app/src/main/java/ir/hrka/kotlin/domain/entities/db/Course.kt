package ir.hrka.kotlin.domain.entities.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "course")
data class Course(
    @PrimaryKey @ColumnInfo(name = "course_id") @SerializedName(value = "course_id") val courseId: Int,
    @ColumnInfo(name = "course_name") @SerializedName(value = "course_name") val courseName: String,
    @ColumnInfo(name = "course_title") @SerializedName(value = "course_title") val courseTitle: String,
    @ColumnInfo(name = "topics_file_name") @SerializedName(value = "topics_file_name") val topicsFileName: String,
    @ColumnInfo(name = "course_description") @SerializedName(value = "course_description") val courseDesc: String,
    @ColumnInfo(name = "course_banner") @SerializedName(value = "course_banner") val courseBanner: String,
    @ColumnInfo(name = "is_active") @SerializedName(value = "is_active") val isActive: Boolean,
) : Parcelable
