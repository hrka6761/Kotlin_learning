package ir.hrka.kotlin.data.repositories.db.read

import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.core.utilities.handleDBResponse
import ir.hrka.kotlin.data.datasource.db.interactions.CourseDao
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.read.ReadCoursesRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadDBCoursesRepoImpl @Inject constructor(
    private val courseDao: CourseDao
) : ReadCoursesRepo {

    override suspend fun getCourses(): Flow<Result<List<Course>?, Errors>> =
        handleDBResponse { courseDao.getCourses() }
}