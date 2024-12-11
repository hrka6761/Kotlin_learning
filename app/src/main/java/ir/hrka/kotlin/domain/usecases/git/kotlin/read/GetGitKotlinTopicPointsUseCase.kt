package ir.hrka.kotlin.domain.usecases.git.kotlin.read

import ir.hrka.kotlin.core.utilities.Course.Kotlin
import ir.hrka.kotlin.domain.repositories.git.ReadGitTopicPointsRepo
import javax.inject.Inject

class GetGitKotlinTopicPointsUseCase @Inject constructor(private val readGitTopicPointsRepo: ReadGitTopicPointsRepo) {

    suspend operator fun invoke(topicName: String) =
        readGitTopicPointsRepo.getTopicPoints(course = Kotlin, topicName = topicName)
}