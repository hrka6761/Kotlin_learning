package ir.hrka.kotlin.domain.repositories.db.read

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.CoroutineTopic

interface ReadDBCoroutineTopicsRepo {

    suspend fun getCoroutineTopicsList(): Resource<List<CoroutineTopic>?>
}