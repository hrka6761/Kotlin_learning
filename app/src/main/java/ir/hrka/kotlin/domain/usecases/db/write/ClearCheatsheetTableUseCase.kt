package ir.hrka.kotlin.domain.usecases.db.write

import ir.hrka.kotlin.domain.repositories.WriteDBCheatsheetRepo
import javax.inject.Inject

class ClearCheatsheetTableUseCase @Inject constructor(private val writeDBCheatsheetRepo: WriteDBCheatsheetRepo) {

    suspend operator fun invoke() = writeDBCheatsheetRepo.clearCheatsheetTable()
}