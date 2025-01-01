package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.ReadCoursesRepo
import javax.inject.Inject
import javax.inject.Named

class GetGitCoursesUseCase @Inject constructor(@Named("git") private val readCoursesRepo: ReadCoursesRepo) {

    suspend operator fun invoke() = readCoursesRepo.getCourses()
}