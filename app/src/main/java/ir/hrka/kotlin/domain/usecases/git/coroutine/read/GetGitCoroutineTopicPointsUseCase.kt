package ir.hrka.kotlin.domain.usecases.git.coroutine.read

import ir.hrka.kotlin.core.utilities.Course.Coroutine
import ir.hrka.kotlin.domain.repositories.git.ReadGitTopicPointsRepo
import javax.inject.Inject

class GetGitCoroutineTopicPointsUseCase @Inject constructor(private val readGitTopicPointsRepo: ReadGitTopicPointsRepo) {

    suspend operator fun invoke(topicName: String) =
        readGitTopicPointsRepo.getTopicPoints(course = Coroutine, topicName = topicName)
}