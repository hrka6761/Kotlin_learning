package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.entities.db.Point
import ir.hrka.kotlin.domain.repositories.WriteCheatsheetRepo
import javax.inject.Inject

class SaveCheatsheetPointsOnDBUseCase @Inject constructor(private val writeCheatsheetRepo: WriteCheatsheetRepo) {

    suspend operator fun invoke(point: Point) = writeCheatsheetRepo.savePointsOnDB(point)
}