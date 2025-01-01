package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.ReadTopicsRepo
import javax.inject.Inject
import javax.inject.Named

class GetDBKotlinTopicsUseCase @Inject constructor(@Named("db") private val readTopicsRepo: ReadTopicsRepo) {

    suspend operator fun invoke() = readTopicsRepo.getKotlinTopics()
}