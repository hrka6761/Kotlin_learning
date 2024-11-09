package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.CheatSheetsRepo
import javax.inject.Inject

class GetCheatSheetsListUseCase @Inject constructor(private val cheatSheetsRepo: CheatSheetsRepo) {

    suspend operator fun invoke() = cheatSheetsRepo.getCheatSheetsList()
}