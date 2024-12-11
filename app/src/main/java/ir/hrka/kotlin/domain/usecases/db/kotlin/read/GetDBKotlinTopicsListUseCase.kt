package ir.hrka.kotlin.domain.usecases.db.kotlin.read

import ir.hrka.kotlin.domain.repositories.db.ReadDBTopicsRepo
import javax.inject.Inject

class GetDBKotlinTopicsListUseCase @Inject constructor(private val readDBTopicsRepo: ReadDBTopicsRepo) {

    suspend operator fun invoke() = readDBTopicsRepo.getKotlinTopicsList()
}