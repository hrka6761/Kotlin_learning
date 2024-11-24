package ir.hrka.kotlin.domain.usecases.db.read

import ir.hrka.kotlin.domain.repositories.ReadGithubCheatSheetRepo
import javax.inject.Inject

class GetDBCheatSheetPointsUseCase @Inject constructor(private val readGithubCheatSheetRepo: ReadGithubCheatSheetRepo) {

    suspend operator fun invoke(fileName: String) = readGithubCheatSheetRepo.getCheatSheetPoints(fileName)
}