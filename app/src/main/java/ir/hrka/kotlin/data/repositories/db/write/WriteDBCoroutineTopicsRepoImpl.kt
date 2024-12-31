package ir.hrka.kotlin.data.repositories.db.write

import ir.hrka.kotlin.core.Constants.DB_CLEAR_COROUTINE_TOPICS_TABLE_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_UPDATE_COROUTINE_TOPICS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_COROUTINE_TOPICS_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.CoroutineTopicsDao
import ir.hrka.kotlin.domain.entities.db.CoroutineTopic
import ir.hrka.kotlin.domain.repositories.db.write.WriteDBCoroutineTopicsRepo
import javax.inject.Inject

class WriteDBCoroutineTopicsRepoImpl @Inject constructor(
    private val coroutineTopicsDao: CoroutineTopicsDao,
) : WriteDBCoroutineTopicsRepo {

    override suspend fun saveCoroutineTopicsListOnDB(topics: List<CoroutineTopic>): Resource<Boolean> {
        return try {
            coroutineTopicsDao.insertTopics(*topics.toTypedArray())
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_WRITE_COROUTINE_TOPICS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun clearCoroutineTopicsListTable(): Resource<Boolean> {
        return try {
            coroutineTopicsDao.deleteTopics()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_CLEAR_COROUTINE_TOPICS_TABLE_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun updateCoroutineTopicStateOnDB(
        id: Int,
        hasContentUpdated: Boolean
    ): Resource<Boolean> {
        return try {
            coroutineTopicsDao.updateTopicState(id, hasContentUpdated)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_UPDATE_COROUTINE_TOPICS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }
}