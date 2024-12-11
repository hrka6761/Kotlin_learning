package ir.hrka.kotlin.data.repositories.db

import ir.hrka.kotlin.core.Constants.DB_CLEAR_COROUTINE_TOPICS_TABLE_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_UPDATE_COROUTINE_TOPICS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_COROUTINE_TOPICS_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.CoroutineTopicsDao
import ir.hrka.kotlin.domain.entities.db.CoroutineTopic
import ir.hrka.kotlin.domain.repositories.db.WriteDBCoroutineTopicRepo
import javax.inject.Inject

class WriteDBCoroutineTopicRepoImpl @Inject constructor(
    private val coroutineTopicsDao: CoroutineTopicsDao,
) : WriteDBCoroutineTopicRepo {

    override suspend fun saveCoroutineTopicsListOnDB(coroutineTopics: List<CoroutineTopic>): Resource<Boolean> {
        return try {
            coroutineTopicsDao.insertTopics(*coroutineTopics.toTypedArray())
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