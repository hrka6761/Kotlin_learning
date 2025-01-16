package ir.hrka.kotlin.domain.usecases.db.topics

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.write.WriteTopicsRepo
import javax.inject.Inject

class UpdateTopicsOnDBUseCase @Inject constructor(private val writeTopicsRepo: WriteTopicsRepo) {

    suspend operator fun invoke(topics: List<Topic>, course: Course): Resource<Boolean?> {
        val removeResult = writeTopicsRepo.removeTopics(course)

        if (removeResult is Resource.Error)
            return removeResult

        return writeTopicsRepo.saveTopics(topics)
    }
}