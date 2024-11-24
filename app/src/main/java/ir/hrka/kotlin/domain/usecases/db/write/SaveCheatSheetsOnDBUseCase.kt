package ir.hrka.kotlin.domain.usecases.db.write

import ir.hrka.kotlin.domain.entities.db.Cheatsheet
import ir.hrka.kotlin.domain.repositories.WriteCheatsheetRepo
import javax.inject.Inject

class SaveCheatSheetsOnDBUseCase @Inject constructor(private val writeCheatsheetRepo: WriteCheatsheetRepo) {

    suspend operator fun invoke(cheatsheets: List<Cheatsheet>) =
        writeCheatsheetRepo.saveCheatSheetsOnDB(cheatsheets)
}