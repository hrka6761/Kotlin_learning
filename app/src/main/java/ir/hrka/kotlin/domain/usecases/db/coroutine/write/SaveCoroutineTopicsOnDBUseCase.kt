package ir.hrka.kotlin.domain.usecases.db.coroutine.write

import ir.hrka.kotlin.domain.entities.db.KotlinTopic
import ir.hrka.kotlin.domain.repositories.db.WriteDBKotlinTopicsRepo
import javax.inject.Inject

class SaveCoroutineTopicsOnDBUseCase @Inject constructor(private val writeDBKotlinTopicsRepo: WriteDBKotlinTopicsRepo) {

    suspend operator fun invoke(cheatsheets: List<KotlinTopic>) =
        writeDBKotlinTopicsRepo.saveKotlinTopicsListOnDB(cheatsheets)
}