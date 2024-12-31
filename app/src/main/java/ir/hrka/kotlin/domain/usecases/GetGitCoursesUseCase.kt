package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.GetCoursesRepo
import javax.inject.Inject
import javax.inject.Named

class GetGitCoursesUseCase @Inject constructor(@Named("git") private val getCoursesRepo: GetCoursesRepo) {

    suspend operator fun invoke() = getCoursesRepo.getCourses()
}