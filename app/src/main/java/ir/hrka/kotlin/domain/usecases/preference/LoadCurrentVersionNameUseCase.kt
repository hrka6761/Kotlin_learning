package ir.hrka.kotlin.domain.usecases.preference

import ir.hrka.kotlin.domain.repositories.LocalDataRepo
import javax.inject.Inject

class LoadCurrentVersionNameUseCase @Inject constructor(private val localDataRepo: LocalDataRepo) {

    suspend operator fun invoke() = localDataRepo.loadCurrentVersionName()
}