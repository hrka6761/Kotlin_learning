package ir.hrka.kotlin.domain.repositories.read

import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.entities.db.Topic
import kotlinx.coroutines.flow.Flow

interface ReadTopicsRepo {

    suspend fun getTopics(course: Course): Flow<Result<List<Topic>?, BaseError>>
}