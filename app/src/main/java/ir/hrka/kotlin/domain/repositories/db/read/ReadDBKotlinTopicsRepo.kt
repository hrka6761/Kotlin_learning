package ir.hrka.kotlin.domain.repositories.db.read

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Topic

interface ReadDBKotlinTopicsRepo {

    suspend fun getKotlinTopicsList(): Resource<List<Topic>?>
}