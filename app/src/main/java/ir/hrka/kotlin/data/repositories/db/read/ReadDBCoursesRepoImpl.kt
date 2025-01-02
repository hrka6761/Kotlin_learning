package ir.hrka.kotlin.data.repositories.db.read

import ir.hrka.kotlin.core.Constants.DB_READ_COURSES_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.CourseDao
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.read.ReadCoursesRepo
import javax.inject.Inject

class ReadDBCoursesRepoImpl @Inject constructor(
    private val courseDao: CourseDao
) : ReadCoursesRepo {

    override suspend fun getCourses(): Resource<List<Course>?> {
        return try {
            Resource.Success(courseDao.getCourses())
        }catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_READ_COURSES_ERROR_CODE,
                    errorMsg = "Can't read courses from the database."
                )
            )
        }
    }
}