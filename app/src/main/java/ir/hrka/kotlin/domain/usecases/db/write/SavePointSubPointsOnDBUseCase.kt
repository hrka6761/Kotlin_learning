package ir.hrka.kotlin.domain.usecases.db.write

import ir.hrka.kotlin.domain.entities.db.SubPoint
import ir.hrka.kotlin.domain.repositories.WriteCheatsheetRepo
import javax.inject.Inject

class SavePointSubPointsOnDBUseCase @Inject constructor(private val writeCheatsheetRepo: WriteCheatsheetRepo) {

    suspend operator fun invoke(subPoints: Array<SubPoint>) =
        writeCheatsheetRepo.saveSubPointsOnDB(subPoints)
}