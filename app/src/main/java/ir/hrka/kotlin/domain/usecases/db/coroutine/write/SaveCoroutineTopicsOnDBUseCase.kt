package ir.hrka.kotlin.domain.usecases.db.coroutine.write

import ir.hrka.kotlin.domain.entities.db.CoroutineTopic
import ir.hrka.kotlin.domain.repositories.db.WriteDBCoroutineTopicsRepo
import javax.inject.Inject

class SaveCoroutineTopicsOnDBUseCase @Inject constructor(private val writeDBCoroutineTopicsRepo: WriteDBCoroutineTopicsRepo) {

    suspend operator fun invoke(topics: List<CoroutineTopic>) =
        writeDBCoroutineTopicsRepo.saveCoroutineTopicsListOnDB(topics)
}