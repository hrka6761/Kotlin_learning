package ir.hrka.kotlin.data.repositories.db.write

import ir.hrka.kotlin.core.Constants.DB_CLEAR_COURSES_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_COURSES_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.CourseDao
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.write.WriteCoursesRepo
import javax.inject.Inject

class WriteDBCoursesRepoImpl @Inject constructor(
    private val courseDao: CourseDao
) : WriteCoursesRepo {

    override suspend fun saveCoursesListOnDB(courses: List<Course>): Resource<Boolean> {
        return try {
            courseDao.insertCourses(*courses.toTypedArray())
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_WRITE_COURSES_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun clearCoursesListTable(): Resource<Boolean> {
        return try {
            courseDao.deleteCourses()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_CLEAR_COURSES_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }
}