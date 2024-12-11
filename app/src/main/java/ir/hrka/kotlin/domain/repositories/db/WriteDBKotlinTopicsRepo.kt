package ir.hrka.kotlin.domain.repositories.db

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.KotlinTopic

interface WriteDBKotlinTopicsRepo {

    suspend fun saveKotlinTopicsListOnDB(kotlinTopics: List<KotlinTopic>): Resource<Boolean>
    suspend fun clearKotlinTopicsListTable(): Resource<Boolean>
    suspend fun updateKotlinTopicStateOnDB(id: Int, hasContentUpdated: Boolean): Resource<Boolean>
}