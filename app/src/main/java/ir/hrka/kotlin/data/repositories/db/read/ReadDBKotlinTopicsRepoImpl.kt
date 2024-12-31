package ir.hrka.kotlin.data.repositories.db.read

import ir.hrka.kotlin.core.Constants.DB_READ_KOTLIN_TOPICS_LIST_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.KotlinTopicsDao
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.domain.entities.db.KotlinTopic
import ir.hrka.kotlin.domain.repositories.db.read.ReadDBKotlinTopicsRepo
import javax.inject.Inject

class ReadDBKotlinTopicsRepoImpl @Inject constructor(
    private val kotlinTopicsDao: KotlinTopicsDao
) : ReadDBKotlinTopicsRepo {

    override suspend fun getKotlinTopicsList(): Resource<List<KotlinTopic>?> {
        return try {
            Resource.Success(kotlinTopicsDao.getTopics())
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_READ_KOTLIN_TOPICS_LIST_ERROR_CODE,
                    errorMsg = "Can't read cheatsheet from the database."
                )
            )
        }
    }
}