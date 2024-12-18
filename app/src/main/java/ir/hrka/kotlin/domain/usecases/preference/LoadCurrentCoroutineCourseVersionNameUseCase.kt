package ir.hrka.kotlin.domain.usecases.preference

import ir.hrka.kotlin.domain.repositories.preference.VersionDataRepo
import javax.inject.Inject

class LoadCurrentCoroutineCourseVersionNameUseCase @Inject constructor(private val versionDataRepo: VersionDataRepo) {

    suspend operator fun invoke() = versionDataRepo.loadCurrentCoroutineCourseVersionName()
}