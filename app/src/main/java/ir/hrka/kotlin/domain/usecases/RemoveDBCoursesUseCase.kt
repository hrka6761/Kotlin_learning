package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.WriteDBCoursesRepo
import javax.inject.Inject

class RemoveDBCoursesUseCase @Inject constructor(private val writeDBCoursesRepo: WriteDBCoursesRepo) {

    suspend operator fun invoke() = writeDBCoursesRepo.clearCoursesListTable()
}