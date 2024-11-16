package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.DBRepo
import javax.inject.Inject

class ClearCheatsheetsOnDBUseCase @Inject constructor(private val dbRepo: DBRepo) {

    suspend operator fun invoke() = dbRepo.clearDB()
}