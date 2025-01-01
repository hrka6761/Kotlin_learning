package ir.hrka.kotlin.data.repositories

import ir.hrka.kotlin.core.Constants.DB_READ_COURSES_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.CoursesDao
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.GetCoursesRepo
import javax.inject.Inject

class DBCoursesRepoImpl @Inject constructor(
    private val coursesDao: CoursesDao
) : GetCoursesRepo {

    override suspend fun getCourses(): Resource<List<Course>?> {
        return try {
            Resource.Success(coursesDao.getCourses())
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