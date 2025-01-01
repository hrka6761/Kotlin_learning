package ir.hrka.kotlin.domain.repositories

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Topic

interface WriteDBKotlinTopicsRepo {

    suspend fun saveKotlinTopicsOnDB(topics: List<Topic>): Resource<Boolean?>
    suspend fun clearKotlinTopicsTable(): Resource<Boolean?>
    suspend fun updateKotlinTopicStateOnDB(id: Int, hasContentUpdated: Boolean): Resource<Boolean?>
}