package ir.hrka.kotlin.data.repositories.db.write

import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.data.datasource.db.interactions.TopicDao
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.core.utilities.handleDBResponse
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.write.WriteTopicsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WriteDBTopicsRepoImpl @Inject constructor(
    private val topicDao: TopicDao
) : WriteTopicsRepo {

    override suspend fun saveTopics(topics: List<Topic>): Flow<Result<Boolean?, Errors>> =
        handleDBResponse {
            topicDao.insertTopics(*topics.toTypedArray())
            return@handleDBResponse true
        }

    override suspend fun removeTopics(course: Course): Flow<Result<Boolean?, Errors>> =
        handleDBResponse {
            topicDao.deleteTopics(course.courseName)
            return@handleDBResponse true
        }

    override suspend fun updateTopicState(
        id: Int,
        hasContentUpdate: Boolean
    ): Flow<Result<Boolean?, Errors>> =
        handleDBResponse {
            topicDao.updateTopicState(id, hasContentUpdate)
            return@handleDBResponse true
        }
}