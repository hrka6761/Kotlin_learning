package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.CheatSheetsRepo
import javax.inject.Inject
import javax.inject.Named

class GetGithubCheatSheetsUseCase @Inject constructor(@Named("Github") private val cheatSheetsRepo: CheatSheetsRepo) {

    suspend operator fun invoke() = cheatSheetsRepo.getCheatSheetsList()
}