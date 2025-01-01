package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.WriteDBKotlinTopicsRepo
import javax.inject.Inject

class RemoveDBKotlinTopicsUseCase @Inject constructor(private val writeDBKotlinTopicsRepo: WriteDBKotlinTopicsRepo) {

    suspend operator fun invoke() = writeDBKotlinTopicsRepo.clearKotlinTopicsTable()
}