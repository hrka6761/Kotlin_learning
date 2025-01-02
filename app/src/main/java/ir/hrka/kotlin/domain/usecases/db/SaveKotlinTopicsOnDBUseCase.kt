package ir.hrka.kotlin.domain.usecases.db

import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.write.WriteTopicsRepo
import javax.inject.Inject

class SaveKotlinTopicsOnDBUseCase @Inject constructor(private val writeTopicsRepo: WriteTopicsRepo) {

    suspend operator fun invoke(kotlinTopics: List<Topic>) =
        writeTopicsRepo.saveKotlinTopicsOnDB(kotlinTopics)
}