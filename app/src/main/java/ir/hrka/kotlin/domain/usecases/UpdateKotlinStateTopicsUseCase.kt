package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.repositories.WriteDBKotlinTopicsRepo
import javax.inject.Inject

class UpdateKotlinStateTopicsUseCase @Inject constructor(private val writeDBKotlinTopicsRepo: WriteDBKotlinTopicsRepo) {

    suspend operator fun invoke(vararg topicsIds: Int, state: Boolean): List<Resource<Boolean?>> {
        val totalResult: MutableList<Resource<Boolean?>> = mutableListOf()

        topicsIds.forEach { topicId ->
            val result = writeDBKotlinTopicsRepo.updateKotlinTopicStateOnDB(topicId, state)
            totalResult.add(result)
        }

        return totalResult
    }
}