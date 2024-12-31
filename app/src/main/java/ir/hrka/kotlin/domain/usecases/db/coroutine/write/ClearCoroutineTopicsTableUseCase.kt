package ir.hrka.kotlin.domain.usecases.db.coroutine.write

import ir.hrka.kotlin.domain.repositories.db.write.WriteDBCoroutineTopicsRepo
import javax.inject.Inject

class ClearCoroutineTopicsTableUseCase @Inject constructor(private val writeDBCoroutineTopicsRepo: WriteDBCoroutineTopicsRepo) {

    suspend operator fun invoke() = writeDBCoroutineTopicsRepo.clearCoroutineTopicsListTable()
}