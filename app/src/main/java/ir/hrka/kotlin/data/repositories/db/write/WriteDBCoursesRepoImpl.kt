package ir.hrka.kotlin.data.repositories.db.write

import ir.hrka.kotlin.core.Constants.DB_REMOVE_COURSES_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_COURSES_ERROR_CODE
import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.data.datasource.db.interactions.CourseDao
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.write.WriteCoursesRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WriteDBCoursesRepoImpl @Inject constructor(
    private val courseDao: CourseDao
) : WriteCoursesRepo {

    override suspend fun saveCourses(courses: List<Course>): Flow<Result<Boolean?, BaseError>> =
        flow {
            emit(Result.Loading)

            try {
                courseDao.insertCourses(*courses.toTypedArray())
                emit(Result.Success(true))
            } catch (e: Exception) {
                emit(
                    Result.Error(
                        Error(
                            errorCode = DB_WRITE_COURSES_ERROR_CODE,
                            errorMsg = e.message.toString()
                        )
                    )
                )
            }
        }


    override suspend fun removeCourses(): Flow<Result<Boolean?, BaseError>> =
        flow {
            emit(Result.Loading)

            try {
                courseDao.deleteCourses()
                emit(Result.Success(true))
            } catch (e: Exception) {
                emit(
                    Result.Error(
                        Error(
                            errorCode = DB_REMOVE_COURSES_ERROR_CODE,
                            errorMsg = e.message.toString()
                        )
                    )
                )
            }
        }
}