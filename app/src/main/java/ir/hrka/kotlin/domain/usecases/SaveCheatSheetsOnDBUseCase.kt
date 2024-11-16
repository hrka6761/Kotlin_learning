package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.entities.db.Cheatsheet
import ir.hrka.kotlin.domain.repositories.DBRepo
import javax.inject.Inject

class SaveCheatSheetsOnDBUseCase @Inject constructor(private val dbRepo: DBRepo) {

    suspend operator fun invoke(cheatsheets: List<Cheatsheet>) =
        dbRepo.saveCheatSheetsOnDB(cheatsheets)
}