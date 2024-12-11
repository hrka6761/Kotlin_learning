package ir.hrka.kotlin.data.repositories.db

import ir.hrka.kotlin.core.Constants.DB_READ_COROUTINE_TOPICS_LIST_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_READ_KOTLIN_TOPICS_LIST_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.CoroutineTopicsDao
import ir.hrka.kotlin.data.datasource.db.interactions.KotlinTopicsDao
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.domain.entities.db.CoroutineTopic
import ir.hrka.kotlin.domain.entities.db.KotlinTopic
import ir.hrka.kotlin.domain.repositories.db.ReadDBTopicsRepo
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class ReadDBTopicsRepoImpl @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val kotlinTopicsDao: KotlinTopicsDao,
    private val coroutineTopicsDao: CoroutineTopicsDao
) : ReadDBTopicsRepo {

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

    override suspend fun getCoroutineTopicsList(): Resource<List<CoroutineTopic>?> {
        return try {
            Resource.Success(coroutineTopicsDao.getTopics())
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_READ_COROUTINE_TOPICS_LIST_ERROR_CODE,
                    errorMsg = "Can't read cheatsheet from the database."
                )
            )
        }
    }
}