package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.GetCoursesRepo
import javax.inject.Inject
import javax.inject.Named

class GetDBCoursesUseCase @Inject constructor(@Named("db") private val getCoursesRepo: GetCoursesRepo) {

    suspend operator fun invoke() = getCoursesRepo.getCourses()
}