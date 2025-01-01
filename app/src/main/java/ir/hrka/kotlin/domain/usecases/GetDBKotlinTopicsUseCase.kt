package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.TopicsRepo
import javax.inject.Inject
import javax.inject.Named

class GetDBKotlinTopicsUseCase @Inject constructor(@Named("db") private val topicsRepo: TopicsRepo) {

    suspend operator fun invoke() = topicsRepo.getKotlinTopics()
}