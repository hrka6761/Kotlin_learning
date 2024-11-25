package ir.hrka.kotlin.domain.usecases.db.read

import ir.hrka.kotlin.domain.repositories.ReadDBCheatSheetRepo
import ir.hrka.kotlin.domain.repositories.ReadGithubCheatSheetRepo
import javax.inject.Inject

class GetDBCheatSheetPointsUseCase @Inject constructor(private val readDBCheatSheetRepo: ReadDBCheatSheetRepo) {

    suspend operator fun invoke(fileName: String) = readDBCheatSheetRepo.getCheatSheetPoints(fileName)
}