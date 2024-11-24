package ir.hrka.kotlin.domain.usecases.github

import ir.hrka.kotlin.domain.repositories.ReadGithubCheatSheetRepo
import javax.inject.Inject

class GetGithubCheatSheetPointsUseCase @Inject constructor(private val readGithubCheatSheetRepo: ReadGithubCheatSheetRepo) {

    suspend operator fun invoke(fileName: String) =
        readGithubCheatSheetRepo.getCheatSheetPoints(fileName)
}