package ir.hrka.kotlin.domain.usecases.preference

import ir.hrka.kotlin.domain.repositories.write.WritePreferencesRepo
import javax.inject.Inject

class SaveCoroutineVersionIdUseCase @Inject constructor(private val writePreferencesRepo: WritePreferencesRepo) {

    suspend operator fun invoke(versionId: Int) =
        writePreferencesRepo.saveCoroutineVersionId(versionId)
}