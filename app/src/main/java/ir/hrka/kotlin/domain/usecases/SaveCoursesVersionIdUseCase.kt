package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.WritePreferencesRepo
import javax.inject.Inject

class SaveCoursesVersionIdUseCase @Inject constructor(private val writePreferencesRepo: WritePreferencesRepo) {

    suspend operator fun invoke(versionId: Int) =
        writePreferencesRepo.saveCoursesVersionId(versionId)
}