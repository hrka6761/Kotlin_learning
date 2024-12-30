package ir.hrka.kotlin.domain.usecases.db.cources

import ir.hrka.kotlin.domain.repositories.db.WriteDBCoursesRepo
import javax.inject.Inject

class ClearDBCoursesUseCase @Inject constructor(private val writeDBCoursesRepo: WriteDBCoursesRepo) {

    suspend operator fun invoke() = writeDBCoursesRepo.clearCoursesListTable()
}