package ir.hrka.kotlin.domain.usecases.db.topics

import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.read.ReadTopicsRepo
import javax.inject.Inject
import javax.inject.Named

class GetTopicsFromDBUseCase @Inject constructor(@Named("db") private val readTopicsRepo: ReadTopicsRepo) {

    suspend operator fun invoke(course: Course) = readTopicsRepo.getTopics(course)
}