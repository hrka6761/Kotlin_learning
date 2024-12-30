package ir.hrka.kotlin.domain.usecases.git.kotlin.read

import ir.hrka.kotlin.domain.repositories.git.CoursesRepo
import javax.inject.Inject
import javax.inject.Named

class GetGitCoursesUseCase @Inject constructor(@Named("git") private val coursesRepo: CoursesRepo) {

    suspend operator fun invoke() = coursesRepo.getCourses()
}