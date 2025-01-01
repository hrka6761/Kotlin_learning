package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.ReadCoursesRepo
import javax.inject.Inject
import javax.inject.Named

class GetDBCoursesUseCase @Inject constructor(@Named("db") private val readCoursesRepo: ReadCoursesRepo) {

    suspend operator fun invoke() = readCoursesRepo.getCourses()
}