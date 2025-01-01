package ir.hrka.kotlin.data.repositories

import ir.hrka.kotlin.core.Constants.DB_READ_COURSES_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.CoroutineTopicsDao
import ir.hrka.kotlin.data.datasource.db.interactions.KotlinTopicsDao
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.TopicsRepo
import javax.inject.Inject

class DBTopicsRepoImpl @Inject constructor(
    private val kotlinTopicsDao: KotlinTopicsDao,
    private val coroutineTopicsDao: CoroutineTopicsDao
) : TopicsRepo{

    override suspend fun getKotlinTopics(): Resource<List<Topic>?> {
        return try {
            Resource.Success(kotlinTopicsDao.getTopics())
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_READ_COURSES_ERROR_CODE,
                    errorMsg = "Can't read courses from the database."
                )
            )
        }
    }

    override suspend fun getCoroutineTopics(): Resource<List<Topic>?> {
        TODO("Not yet implemented")
    }
}