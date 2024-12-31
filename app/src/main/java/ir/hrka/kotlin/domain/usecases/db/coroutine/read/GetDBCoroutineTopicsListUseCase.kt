package ir.hrka.kotlin.domain.usecases.db.coroutine.read

import ir.hrka.kotlin.domain.repositories.db.read.ReadDBCoroutineTopicsRepo
import ir.hrka.kotlin.domain.repositories.db.read.ReadDBKotlinTopicsRepo
import javax.inject.Inject

class GetDBCoroutineTopicsListUseCase @Inject constructor(private val readDBCoroutineTopicsRepo: ReadDBCoroutineTopicsRepo) {

    suspend operator fun invoke() = readDBCoroutineTopicsRepo.getCoroutineTopicsList()
}