package ir.hrka.kotlin.domain.usecases.preference

import ir.hrka.kotlin.domain.repositories.preference.VersionLocalInfoRepo
import javax.inject.Inject

class GetCoroutineVersionIdUseCase @Inject constructor(private val versionLocalInfoRepo: VersionLocalInfoRepo) {

    suspend operator fun invoke() = versionLocalInfoRepo.loadCoroutineVersionId()
}