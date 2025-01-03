package ir.hrka.kotlin.domain.usecases.db.topics

import ir.hrka.kotlin.core.utilities.Course
import ir.hrka.kotlin.domain.repositories.read.ReadTopicsRepo
import javax.inject.Inject
import javax.inject.Named

class GetDBTopicsUseCase @Inject constructor(@Named("db") private val readTopicsRepo: ReadTopicsRepo) {

    suspend operator fun invoke(course: Course) = readTopicsRepo.getTopics(course)
}