package ir.hrka.kotlin.domain.usecases.db.kotlin.write

import ir.hrka.kotlin.domain.repositories.db.write.WriteDBKotlinTopicsRepo
import javax.inject.Inject

class ClearKotlinTopicsTableUseCase @Inject constructor(private val writeDBKotlinTopicsRepo: WriteDBKotlinTopicsRepo) {

    suspend operator fun invoke() = writeDBKotlinTopicsRepo.clearKotlinTopicsListTable()
}