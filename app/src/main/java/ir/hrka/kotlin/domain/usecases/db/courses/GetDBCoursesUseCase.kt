package ir.hrka.kotlin.domain.usecases.db.courses

import ir.hrka.kotlin.domain.repositories.read.ReadCoursesRepo
import javax.inject.Inject
import javax.inject.Named

class GetDBCoursesUseCase @Inject constructor(@Named("db") private val readCoursesRepo: ReadCoursesRepo) {

    suspend operator fun invoke() = readCoursesRepo.getCourses()
}