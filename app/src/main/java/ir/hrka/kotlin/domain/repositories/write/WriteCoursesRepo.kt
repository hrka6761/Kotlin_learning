package ir.hrka.kotlin.domain.repositories.write

import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.domain.entities.db.Course
import kotlinx.coroutines.flow.Flow

interface WriteCoursesRepo {

    suspend fun saveCourses(courses: List<Course>): Flow<Result<Boolean?, BaseError>>
    suspend fun removeCourses(): Flow<Result<Boolean?, BaseError>>
}