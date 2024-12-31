package ir.hrka.kotlin.data.repositories.db.read

import ir.hrka.kotlin.core.Constants.DB_READ_COROUTINE_TOPICS_LIST_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.CoroutineTopicsDao
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.domain.entities.db.CoroutineTopic
import ir.hrka.kotlin.domain.repositories.db.read.ReadDBCoroutineTopicsRepo
import javax.inject.Inject

class ReadDBCoroutineTopicsRepoImpl @Inject constructor(
    private val coroutineTopicsDao: CoroutineTopicsDao
) : ReadDBCoroutineTopicsRepo {

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