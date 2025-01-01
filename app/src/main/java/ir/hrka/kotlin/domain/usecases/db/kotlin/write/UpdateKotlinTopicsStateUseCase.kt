package ir.hrka.kotlin.domain.usecases.db.kotlin.write

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.repositories.WriteKotlinTopicsRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class UpdateKotlinTopicsStateUseCase @Inject constructor(private val writeKotlinTopicsRepo: WriteKotlinTopicsRepo) {

    suspend operator fun invoke(vararg ids: Int, hasContentUpdated: Boolean): Resource<Boolean?> {
        ids.forEach { id ->
            val differed = CoroutineScope(Dispatchers.IO).async {
                writeKotlinTopicsRepo.updateKotlinTopicStateOnDB(id, hasContentUpdated)
            }
            val updateResult = differed.await()

            if (updateResult is Resource.Error)
                return updateResult
        }

        return Resource.Success(true)
    }
}