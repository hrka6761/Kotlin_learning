package ir.hrka.kotlin.domain.entities.git

import com.google.gson.annotations.SerializedName
import ir.hrka.kotlin.domain.entities.db.Course

data class CoursesData(
    @SerializedName("course_number") val courseNumber: Int,
    @SerializedName("courses") val courses: List<Course>
) : Data<List<Course>> {

    override fun getMasterData(): List<Course> = courses
}