package ir.hrka.kotlin.domain.usecases.db.points

import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.read.ReadPointsRepo
import javax.inject.Inject
import javax.inject.Named

class GetPointsFromDBUseCase @Inject constructor(@Named("db") private val readPointsRepo: ReadPointsRepo) {

    suspend operator fun invoke(topic: Topic) = readPointsRepo.getPoints(topic)
}