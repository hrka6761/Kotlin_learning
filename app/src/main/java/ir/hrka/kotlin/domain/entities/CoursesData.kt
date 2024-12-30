package ir.hrka.kotlin.domain.entities

import com.google.gson.annotations.SerializedName
import ir.hrka.kotlin.domain.entities.db.Course

data class CoursesData(
    @SerializedName("course_number") val courseNumber: Int,
    @SerializedName("courses") val courses: List<Course>
)