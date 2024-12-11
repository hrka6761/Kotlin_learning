package ir.hrka.kotlin.domain.usecases.db.coroutine.write

import ir.hrka.kotlin.domain.repositories.db.WriteDBTopicPointsRepo
import javax.inject.Inject

class DeleteCoroutineTopicPointsUseCase @Inject constructor(private val writeDBTopicsPointsRepo: WriteDBTopicPointsRepo) {

    suspend operator fun invoke(cheatsheetName: String) =
        writeDBTopicsPointsRepo.deletePoints(cheatsheetName)
}