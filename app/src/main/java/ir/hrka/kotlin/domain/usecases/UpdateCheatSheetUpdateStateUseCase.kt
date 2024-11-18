package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.repositories.DBRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class UpdateCheatSheetUpdateStateUseCase @Inject constructor(private val dbRepo: DBRepo) {

    suspend operator fun invoke(vararg ids: Int, hasContentUpdated: Boolean): Resource<Boolean> {
        ids.forEach { id ->
            val differed = CoroutineScope(Dispatchers.IO).async {
                dbRepo.updateCheatsheetUpdateStateOnDB(id, hasContentUpdated)
            }
            val updateResult = differed.await()

            if (updateResult is Resource.Error)
                return updateResult
        }

        return Resource.Success(true)
    }
}