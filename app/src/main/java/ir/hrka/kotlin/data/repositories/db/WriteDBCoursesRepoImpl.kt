package ir.hrka.kotlin.data.repositories.db

import ir.hrka.kotlin.core.Constants.DB_CLEAR_COURSES_TABLE_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_COURSES_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.CoursesDao
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.db.WriteDBCoursesRepo
import javax.inject.Inject

class WriteDBCoursesRepoImpl @Inject constructor(
    private val coursesDao: CoursesDao
) : WriteDBCoursesRepo {

    override suspend fun saveCoursesListOnDB(courses: List<Course>): Resource<Boolean> {
        return try {
            coursesDao.insertCourses(*courses.toTypedArray())
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
            coursesDao.deleteCourses()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_CLEAR_COURSES_TABLE_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }
}