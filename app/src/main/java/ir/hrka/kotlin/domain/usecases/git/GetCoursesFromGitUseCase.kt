package ir.hrka.kotlin.domain.usecases.git

import ir.hrka.kotlin.domain.repositories.read.ReadCoursesRepo
import javax.inject.Inject
import javax.inject.Named

class GetCoursesFromGitUseCase @Inject constructor(@Named("git") private val readCoursesRepo: ReadCoursesRepo) {

    suspend operator fun invoke() = readCoursesRepo.getCourses()
}