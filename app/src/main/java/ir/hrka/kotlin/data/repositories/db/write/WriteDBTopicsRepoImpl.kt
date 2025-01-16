package ir.hrka.kotlin.data.repositories.db.write

import ir.hrka.kotlin.core.Constants.DB_CLEAR_TOPICS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_UPDATE_TOPICS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_TOPICS_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.TopicDao
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.write.WriteTopicsRepo
import javax.inject.Inject

class WriteDBTopicsRepoImpl @Inject constructor(
    private val topicDao: TopicDao
) : WriteTopicsRepo {

    override suspend fun saveTopics(topics: List<Topic>): Resource<Boolean?> {
        return try {
            topicDao.insertTopics(*topics.toTypedArray())
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_WRITE_TOPICS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun removeTopics(course: Course): Resource<Boolean?> {
        return try {
            topicDao.deleteTopics(course.courseName)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_CLEAR_TOPICS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun updateTopicState(
        id: Int,
        hasContentUpdate: Boolean
    ): Resource<Boolean?> {
        return try {
            topicDao.updateTopicState(id, hasContentUpdate)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_UPDATE_TOPICS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }
}