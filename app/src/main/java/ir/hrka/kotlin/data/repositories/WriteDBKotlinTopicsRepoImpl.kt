package ir.hrka.kotlin.data.repositories

import ir.hrka.kotlin.core.Constants.DB_CLEAR_KOTLIN_TOPICS_TABLE_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_UPDATE_KOTLIN_TOPICS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_KOTLIN_TOPICS_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.KotlinTopicsDao
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.WriteDBKotlinTopicsRepo
import javax.inject.Inject

class WriteDBKotlinTopicsRepoImpl @Inject constructor(
    private val kotlinTopicsDao: KotlinTopicsDao
) : WriteDBKotlinTopicsRepo {

    override suspend fun saveKotlinTopicsOnDB(topics: List<Topic>): Resource<Boolean?> {
        return try {
            kotlinTopicsDao.insertTopics(*topics.toTypedArray())
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_WRITE_KOTLIN_TOPICS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun clearKotlinTopicsTable(): Resource<Boolean?> {
        return try {
            kotlinTopicsDao.deleteTopics()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_CLEAR_KOTLIN_TOPICS_TABLE_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun updateKotlinTopicStateOnDB(
        id: Int,
        hasContentUpdated: Boolean
    ): Resource<Boolean?> {
        return try {
            kotlinTopicsDao.updateTopicState(id, hasContentUpdated)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_UPDATE_KOTLIN_TOPICS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }
}