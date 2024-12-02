package ir.hrka.kotlin.domain.usecases.db.write

import ir.hrka.kotlin.domain.entities.db.Cheatsheet
import ir.hrka.kotlin.domain.repositories.WriteDBCheatsheetRepo
import javax.inject.Inject

class SaveCheatSheetsOnDBUseCase @Inject constructor(private val writeDBCheatsheetRepo: WriteDBCheatsheetRepo) {

    suspend operator fun invoke(cheatsheets: List<Cheatsheet>) =
        writeDBCheatsheetRepo.saveCheatSheetsOnDB(cheatsheets)
}