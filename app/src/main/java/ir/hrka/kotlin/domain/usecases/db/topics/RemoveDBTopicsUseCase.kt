package ir.hrka.kotlin.domain.usecases.db.topics

import ir.hrka.kotlin.core.utilities.Course
import ir.hrka.kotlin.domain.repositories.write.WriteTopicsRepo
import javax.inject.Inject

class RemoveDBTopicsUseCase @Inject constructor(private val writeTopicsRepo: WriteTopicsRepo) {

    suspend operator fun invoke(course: Course) = writeTopicsRepo.removeTopics(course)
}