package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.WriteKotlinTopicsRepo
import javax.inject.Inject

class SaveKotlinTopicsOnDBUseCase @Inject constructor(private val writeKotlinTopicsRepo: WriteKotlinTopicsRepo) {

    suspend operator fun invoke(kotlinTopics: List<Topic>) =
        writeKotlinTopicsRepo.saveKotlinTopicsOnDB(kotlinTopics)
}