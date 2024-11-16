package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.CheatSheetsRepo
import javax.inject.Inject
import javax.inject.Named

class GetDBCheatsheetsUseCase @Inject constructor(@Named("db") private val cheatSheetsRepo: CheatSheetsRepo) {

    suspend operator fun invoke() = cheatSheetsRepo.getCheatSheetsList()
}