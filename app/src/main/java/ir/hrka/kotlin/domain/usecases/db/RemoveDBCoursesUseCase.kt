package ir.hrka.kotlin.domain.usecases.db

import ir.hrka.kotlin.domain.repositories.write.WriteCoursesRepo
import javax.inject.Inject

class RemoveDBCoursesUseCase @Inject constructor(private val writeCoursesRepo: WriteCoursesRepo) {

    suspend operator fun invoke() = writeCoursesRepo.clearCoursesListTable()
}