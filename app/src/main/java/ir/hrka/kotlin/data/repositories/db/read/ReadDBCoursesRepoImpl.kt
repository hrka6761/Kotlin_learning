package ir.hrka.kotlin.data.repositories.db.read

import ir.hrka.kotlin.core.Constants.DB_READ_COURSES_ERROR_CODE
import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.data.datasource.db.interactions.CourseDao
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.read.ReadCoursesRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReadDBCoursesRepoImpl @Inject constructor(
    private val courseDao: CourseDao
) : ReadCoursesRepo {

    override suspend fun getCourses(): Flow<Result<List<Course>?, BaseError>> =
        flow {
            emit(Result.Loading)
            try {
                emit(Result.Success(courseDao.getCourses()))
            } catch (e: Exception) {
                emit(
                    Result.Error(
                        Error(
                            errorCode = DB_READ_COURSES_ERROR_CODE,
                            errorMsg = "Can't read courses from the database."
                        )
                    )
                )
            }
        }
}