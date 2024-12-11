package ir.hrka.kotlin.data.repositories.db

import ir.hrka.kotlin.core.Constants.DB_CLEAR_KOTLIN_TOPICS_TABLE_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_UPDATE_KOTLIN_TOPICS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_KOTLIN_TOPICS_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.KotlinTopicsDao
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.domain.entities.db.KotlinTopic
import ir.hrka.kotlin.domain.repositories.db.WriteDBKotlinTopicsRepo
import javax.inject.Inject

class WriteDBKotlinTopicsRepoImpl @Inject constructor(
    private val kotlinTopicsDao: KotlinTopicsDao
) : WriteDBKotlinTopicsRepo {

    override suspend fun saveKotlinTopicsListOnDB(kotlinTopics: List<KotlinTopic>): Resource<Boolean> {
        return try {
            kotlinTopicsDao.insertTopics(*kotlinTopics.toTypedArray())
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

    override suspend fun clearKotlinTopicsListTable(): Resource<Boolean> {
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
    ): Resource<Boolean> {
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