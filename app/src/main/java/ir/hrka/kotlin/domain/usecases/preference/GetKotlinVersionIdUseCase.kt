package ir.hrka.kotlin.domain.usecases.preference

import ir.hrka.kotlin.domain.repositories.read.ReadPreferencesRepo
import javax.inject.Inject

class GetKotlinVersionIdUseCase @Inject constructor(private val readPreferencesRepo: ReadPreferencesRepo) {

    suspend operator fun invoke() = readPreferencesRepo.loadKotlinVersionId()
}