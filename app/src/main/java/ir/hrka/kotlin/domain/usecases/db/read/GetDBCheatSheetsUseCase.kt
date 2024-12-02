package ir.hrka.kotlin.domain.usecases.db.read

import ir.hrka.kotlin.domain.repositories.db.ReadDBCheatSheetRepo
import javax.inject.Inject

class GetDBCheatSheetsUseCase @Inject constructor(
    private val readDBCheatSheetRepo: ReadDBCheatSheetRepo
) {

    suspend operator fun invoke() = readDBCheatSheetRepo.getCheatSheetsList()
}