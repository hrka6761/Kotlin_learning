package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.ReadCheatSheetRepo
import javax.inject.Inject
import javax.inject.Named

class GetGithubCheatSheetPointsUseCase @Inject constructor(@Named("Github") private val readCheatSheetRepo: ReadCheatSheetRepo) {

    suspend operator fun invoke(fileName: String) = readCheatSheetRepo.getCheatSheetPoints(fileName)
}