package ir.hrka.kotlin.domain.usecases.db.cources

import ir.hrka.kotlin.domain.repositories.git.CoursesRepo
import javax.inject.Inject
import javax.inject.Named

class GetDBCoursesUseCase @Inject constructor(@Named("db") private val coursesRepo: CoursesRepo) {

    suspend operator fun invoke() = coursesRepo.getCourses()
}