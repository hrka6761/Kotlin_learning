package ir.hrka.kotlin.domain.usecases.preference

import ir.hrka.kotlin.domain.repositories.LocalDataRepo
import javax.inject.Inject

class SaveCurrentVersionNameUseCase @Inject constructor(private val localDataRepo: LocalDataRepo) {

    suspend operator fun invoke(versionName: String) = localDataRepo.saveCurrentVersionName(versionName)
}