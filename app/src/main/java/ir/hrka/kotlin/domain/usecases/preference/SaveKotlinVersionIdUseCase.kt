package ir.hrka.kotlin.domain.usecases.preference

import ir.hrka.kotlin.domain.repositories.write.WritePreferencesRepo
import javax.inject.Inject

class SaveKotlinVersionIdUseCase @Inject constructor(private val writePreferencesRepo: WritePreferencesRepo) {

    suspend operator fun invoke(versionId: Int) = writePreferencesRepo.saveKotlinVersionId(versionId)
}