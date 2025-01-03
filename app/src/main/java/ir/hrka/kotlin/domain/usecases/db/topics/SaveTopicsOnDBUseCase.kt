package ir.hrka.kotlin.domain.usecases.db.topics

import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.write.WriteTopicsRepo
import javax.inject.Inject

class SaveTopicsOnDBUseCase @Inject constructor(private val writeTopicsRepo: WriteTopicsRepo) {

    suspend operator fun invoke(topics: List<Topic>) = writeTopicsRepo.saveTopicsOnDB(topics)
}