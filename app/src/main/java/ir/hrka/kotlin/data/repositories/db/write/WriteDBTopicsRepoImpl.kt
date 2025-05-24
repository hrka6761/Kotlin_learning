package ir.hrka.kotlin.data.repositories.db.write

import ir.hrka.kotlin.core.Constants.DB_CLEAR_TOPICS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_UPDATE_TOPICS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_TOPICS_ERROR_CODE
import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.data.datasource.db.interactions.TopicDao
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.write.WriteTopicsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WriteDBTopicsRepoImpl @Inject constructor(
    private val topicDao: TopicDao
) : WriteTopicsRepo {

    override suspend fun saveTopics(topics: List<Topic>): Flow<Result<Boolean?, BaseError>> =
        flow {
            emit(Result.Loading)

            try {
                topicDao.insertTopics(*topics.toTypedArray())
                emit(Result.Success(true))
            } catch (e: Exception) {
                emit(
                    Result.Error(
                        Error(
                            errorCode = DB_WRITE_TOPICS_ERROR_CODE,
                            errorMsg = e.message.toString()
                        )
                    )
                )
            }
        }


    override suspend fun removeTopics(course: Course): Flow<Result<Boolean?, BaseError>> =
        flow {
            emit(Result.Loading)

            try {
                topicDao.deleteTopics(course.courseName)
                emit(Result.Success(true))
            } catch (e: Exception) {
                emit(
                    Result.Error(
                        Error(
                            errorCode = DB_CLEAR_TOPICS_ERROR_CODE,
                            errorMsg = e.message.toString()
                        )
                    )
                )
            }
        }

    override suspend fun updateTopicState(
        id: Int,
        hasContentUpdate: Boolean
    ): Flow<Result<Boolean?, BaseError>> =
        flow {
            emit(Result.Loading)

            try {
                topicDao.updateTopicState(id, hasContentUpdate)
                emit(Result.Success(true))
            } catch (e: Exception) {
                emit(
                    Result.Error(
                        Error(
                            errorCode = DB_UPDATE_TOPICS_ERROR_CODE,
                            errorMsg = e.message.toString()
                        )
                    )
                )
            }
        }
}