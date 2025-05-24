package ir.hrka.kotlin.domain.repositories.write

import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.entities.db.Topic
import kotlinx.coroutines.flow.Flow

interface WriteTopicsRepo {

    suspend fun saveTopics(topics: List<Topic>): Flow<Result<Boolean?, BaseError>>
    suspend fun removeTopics(course: Course): Flow<Result<Boolean?, BaseError>>
    suspend fun updateTopicState(
        id: Int,
        hasContentUpdate: Boolean
    ): Flow<Result<Boolean?, BaseError>>
}