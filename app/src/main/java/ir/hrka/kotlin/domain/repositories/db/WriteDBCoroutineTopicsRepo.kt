package ir.hrka.kotlin.domain.repositories.db

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.CoroutineTopic

interface WriteDBCoroutineTopicsRepo {

    suspend fun saveCoroutineTopicsListOnDB(topics: List<CoroutineTopic>): Resource<Boolean>
    suspend fun clearCoroutineTopicsListTable(): Resource<Boolean>
    suspend fun updateCoroutineTopicStateOnDB(id: Int, hasContentUpdated: Boolean): Resource<Boolean>
}