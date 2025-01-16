package ir.hrka.kotlin.domain.usecases.git

import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.read.ReadTopicsRepo
import javax.inject.Inject
import javax.inject.Named

class GetTopicsFromGitUseCase @Inject constructor(@Named("git") private val readTopicsRepo: ReadTopicsRepo) {

    suspend operator fun invoke(course: Course) = readTopicsRepo.getTopics(course)
}