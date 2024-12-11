package ir.hrka.kotlin.domain.repositories.db

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.CoroutineTopic
import ir.hrka.kotlin.domain.entities.db.KotlinTopic

interface ReadDBTopicsRepo {

    suspend fun getKotlinTopicsList(): Resource<List<KotlinTopic>?>
    suspend fun getCoroutineTopicsList(): Resource<List<CoroutineTopic>?>
}