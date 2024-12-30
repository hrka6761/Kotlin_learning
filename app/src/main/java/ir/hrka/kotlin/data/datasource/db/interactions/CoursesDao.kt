package ir.hrka.kotlin.data.datasource.db.interactions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ir.hrka.kotlin.domain.entities.db.Course

@Dao
interface CoursesDao {

    @Insert
    suspend fun insertCourses(vararg courses: Course)

    @Query("SELECT * FROM course")
    suspend fun getCourses(): List<Course>

    @Query("DELETE FROM course")
    suspend fun deleteCourses()
}