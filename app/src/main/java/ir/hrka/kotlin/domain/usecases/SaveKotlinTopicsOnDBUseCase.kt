package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.WriteDBKotlinTopicsRepo
import javax.inject.Inject

class SaveKotlinTopicsOnDBUseCase @Inject constructor(private val writeDBKotlinTopicsRepo: WriteDBKotlinTopicsRepo) {

    suspend operator fun invoke(kotlinTopics: List<Topic>) =
        writeDBKotlinTopicsRepo.saveKotlinTopicsOnDB(kotlinTopics)
}