package ir.hrka.kotlin.domain.usecases.preference

import ir.hrka.kotlin.domain.repositories.VersionDataRepo
import javax.inject.Inject

class SaveCurrentVersionNameUseCase @Inject constructor(private val versionDataRepo: VersionDataRepo) {

    suspend operator fun invoke(versionName: String) = versionDataRepo.saveCurrentVersionName(versionName)
}