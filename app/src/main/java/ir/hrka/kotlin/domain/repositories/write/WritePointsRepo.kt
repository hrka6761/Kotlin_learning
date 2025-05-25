package ir.hrka.kotlin.domain.repositories.write

import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.domain.entities.db.DBPoint
import kotlinx.coroutines.flow.Flow

interface WritePointsRepo {

    suspend fun savePoint(DBPoint: DBPoint): Flow<Result<Long?, Errors>>
    suspend fun removePoint(id: Long): Flow<Result<Boolean?, Errors>>
}