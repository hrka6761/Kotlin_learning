package ir.hrka.kotlin.domain.usecases.db.coroutine.read

import ir.hrka.kotlin.domain.repositories.db.ReadDBTopicPointsRepo
import ir.hrka.kotlin.domain.repositories.db.ReadDBTopicsRepo
import javax.inject.Inject

class GetDBCoroutineTopicPointsUseCase @Inject constructor(private val readDBTopicPointsRepo: ReadDBTopicPointsRepo) {

    suspend operator fun invoke(topicName: String) = readDBTopicPointsRepo.getTopicPoints(topicName)
}