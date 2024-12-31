package ir.hrka.kotlin.domain.usecases.db.kotlin.read

import ir.hrka.kotlin.domain.repositories.db.read.ReadDBKotlinTopicsRepo
import javax.inject.Inject

class GetDBKotlinTopicsListUseCase @Inject constructor(private val readDBKotlinTopicsRepo: ReadDBKotlinTopicsRepo) {

    suspend operator fun invoke() = readDBKotlinTopicsRepo.getKotlinTopicsList()
}