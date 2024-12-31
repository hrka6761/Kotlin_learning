package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.VersionInfoRepo
import javax.inject.Inject

class GetCoursesVersionIdUseCase @Inject constructor(private val versionInfoRepo: VersionInfoRepo) {

    suspend operator fun invoke() = versionInfoRepo.loadCoursesVersionId()
}