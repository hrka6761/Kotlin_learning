package ir.hrka.kotlin.data.repositories.db.write

import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.core.utilities.handleDBResponse
import ir.hrka.kotlin.data.datasource.db.interactions.CourseDao
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.write.WriteCoursesRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WriteDBCoursesRepoImpl @Inject constructor(
    private val courseDao: CourseDao
) : WriteCoursesRepo {

    override suspend fun saveCourses(courses: List<Course>): Flow<Result<Boolean?, Errors>> =
        handleDBResponse {
            courseDao.insertCourses(*courses.toTypedArray())
            return@handleDBResponse true
        }

    override suspend fun removeCourses(): Flow<Result<Boolean?, Errors>> =
        handleDBResponse {
            courseDao.deleteCourses()
            return@handleDBResponse true
        }
}