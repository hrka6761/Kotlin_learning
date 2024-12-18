package ir.hrka.kotlin.domain.usecases.preference

import ir.hrka.kotlin.domain.repositories.preference.VersionDataRepo
import javax.inject.Inject

class SaveCurrentCoroutineCourseVersionNameUseCase @Inject constructor(private val versionDataRepo: VersionDataRepo) {

    suspend operator fun invoke(versionName: String) = versionDataRepo.saveCurrentCoroutineCourseVersionName(versionName)
}