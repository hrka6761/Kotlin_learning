package ir.hrka.kotlin.domain.repositories.read

import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.domain.entities.db.Course
import kotlinx.coroutines.flow.Flow

interface ReadCoursesRepo {

    suspend fun getCourses(): Flow<Result<List<Course>?, BaseError>>
}