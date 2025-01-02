package ir.hrka.kotlin.domain.usecases.db

import ir.hrka.kotlin.domain.repositories.write.WriteTopicsRepo
import javax.inject.Inject

class RemoveDBKotlinTopicsUseCase @Inject constructor(private val writeTopicsRepo: WriteTopicsRepo) {

    suspend operator fun invoke() = writeTopicsRepo.clearKotlinTopicsTable()
}