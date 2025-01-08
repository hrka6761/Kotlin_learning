package ir.hrka.kotlin.domain.usecases.db.topics

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.repositories.write.WriteTopicsRepo
import javax.inject.Inject

class UpdateTopicsStateOnDBUseCase @Inject constructor(private val writeTopicsRepo: WriteTopicsRepo) {

    suspend operator fun invoke(vararg topicsIds: Int, state: Boolean): List<Resource<Boolean?>> {
        val totalResult: MutableList<Resource<Boolean?>> = mutableListOf()

        topicsIds.forEach { topicId ->
            val result = writeTopicsRepo.updateTopicState(topicId, state)
            totalResult.add(result)
        }

        return totalResult
    }
}