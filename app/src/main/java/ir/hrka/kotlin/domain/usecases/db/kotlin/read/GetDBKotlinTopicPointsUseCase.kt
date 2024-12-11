package ir.hrka.kotlin.domain.usecases.db.kotlin.read

import ir.hrka.kotlin.domain.repositories.db.ReadDBTopicPointsRepo
import javax.inject.Inject

class GetDBKotlinTopicPointsUseCase @Inject constructor(private val readDBTopicPointsRepo: ReadDBTopicPointsRepo) {

    suspend operator fun invoke(topicName: String) = readDBTopicPointsRepo.getTopicPoints(topicName)
}