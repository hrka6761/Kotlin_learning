package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.WriteKotlinTopicsRepo
import javax.inject.Inject

class RemoveDBKotlinTopicsUseCase @Inject constructor(private val writeKotlinTopicsRepo: WriteKotlinTopicsRepo) {

    suspend operator fun invoke() = writeKotlinTopicsRepo.clearKotlinTopicsTable()
}