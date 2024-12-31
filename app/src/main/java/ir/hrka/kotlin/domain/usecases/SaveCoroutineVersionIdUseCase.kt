package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.VersionInfoRepo
import javax.inject.Inject

class SaveCoroutineVersionIdUseCase @Inject constructor(private val versionInfoRepo: VersionInfoRepo) {

    suspend operator fun invoke(versionId: Int) =
        versionInfoRepo.saveCoroutineVersionId(versionId)
}