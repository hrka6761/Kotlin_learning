package ir.hrka.kotlin.domain.usecases.preference

import ir.hrka.kotlin.domain.repositories.preference.LocalVersionInfoRepo
import javax.inject.Inject

class GetCoursesVersionIdUseCase @Inject constructor(private val localVersionInfoRepo: LocalVersionInfoRepo) {

    suspend operator fun invoke() = localVersionInfoRepo.loadCoursesVersionId()
}