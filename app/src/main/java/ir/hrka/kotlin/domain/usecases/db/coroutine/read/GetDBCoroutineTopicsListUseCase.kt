package ir.hrka.kotlin.domain.usecases.db.coroutine.read

import ir.hrka.kotlin.domain.repositories.db.ReadDBTopicsRepo
import javax.inject.Inject

class GetDBCoroutineTopicsListUseCase @Inject constructor(private val readDBTopicsRepo: ReadDBTopicsRepo) {

    suspend operator fun invoke() = readDBTopicsRepo.getCoroutineTopicsList()
}