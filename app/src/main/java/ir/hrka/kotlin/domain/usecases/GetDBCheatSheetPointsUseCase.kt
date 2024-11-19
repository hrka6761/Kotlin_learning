package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.ReadCheatSheetRepo
import javax.inject.Inject
import javax.inject.Named

class GetDBCheatSheetPointsUseCase @Inject constructor(@Named("db") private val readCheatSheetRepo: ReadCheatSheetRepo) {

    suspend operator fun invoke(fileName: String) = readCheatSheetRepo.getCheatSheetPoints(fileName)
}