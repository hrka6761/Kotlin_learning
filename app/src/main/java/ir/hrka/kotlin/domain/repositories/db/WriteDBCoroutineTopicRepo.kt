package ir.hrka.kotlin.domain.repositories.db

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.CoroutineTopic

interface WriteDBCoroutineTopicRepo {

    suspend fun saveCoroutineTopicsListOnDB(coroutineTopics: List<CoroutineTopic>): Resource<Boolean>
    suspend fun clearCoroutineTopicsListTable(): Resource<Boolean>
    suspend fun updateCoroutineTopicStateOnDB(id: Int, hasContentUpdated: Boolean): Resource<Boolean>
}