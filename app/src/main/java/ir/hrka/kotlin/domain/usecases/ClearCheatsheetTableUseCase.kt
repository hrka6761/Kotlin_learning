package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.WriteCheatsheetRepo
import javax.inject.Inject

class ClearCheatsheetTableUseCase @Inject constructor(private val writeCheatsheetRepo: WriteCheatsheetRepo) {

    suspend operator fun invoke() = writeCheatsheetRepo.clearCheatsheetTable()
}