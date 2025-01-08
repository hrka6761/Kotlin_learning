package ir.hrka.kotlin.domain.usecases.git

import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.read.ReadPointsRepo
import javax.inject.Inject
import javax.inject.Named

class GetPointsFromGitUseCase @Inject constructor(@Named("git") private val readPointsRepo: ReadPointsRepo) {

    suspend operator fun invoke(topic: Topic) = readPointsRepo.getPoints(topic)
}