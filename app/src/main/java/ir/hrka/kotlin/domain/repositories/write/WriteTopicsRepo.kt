package ir.hrka.kotlin.domain.repositories.write

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Topic

interface WriteTopicsRepo {

    suspend fun saveKotlinTopicsOnDB(topics: List<Topic>): Resource<Boolean?>
    suspend fun clearKotlinTopicsTable(): Resource<Boolean?>
    suspend fun updateKotlinTopicStateOnDB(id: Int, hasContentUpdated: Boolean): Resource<Boolean?>
}