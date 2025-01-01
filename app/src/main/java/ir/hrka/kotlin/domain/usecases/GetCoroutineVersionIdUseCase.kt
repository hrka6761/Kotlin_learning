package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.ReadPreferencesRepo
import javax.inject.Inject

class GetCoroutineVersionIdUseCase @Inject constructor(private val readPreferencesRepo: ReadPreferencesRepo) {

    suspend operator fun invoke() = readPreferencesRepo.loadCoroutineVersionId()
}