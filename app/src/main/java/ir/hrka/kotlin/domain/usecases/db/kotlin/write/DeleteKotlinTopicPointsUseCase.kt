package ir.hrka.kotlin.domain.usecases.db.kotlin.write

import ir.hrka.kotlin.domain.repositories.db.WriteDBKotlinTopicsRepo
import ir.hrka.kotlin.domain.repositories.db.WriteDBTopicPointsRepo
import javax.inject.Inject

class DeleteKotlinTopicPointsUseCase @Inject constructor(private val writeDBTopicPointsRepo: WriteDBTopicPointsRepo) {

    suspend operator fun invoke(cheatsheetName: String) =
        writeDBTopicPointsRepo.deletePoints(cheatsheetName)
}