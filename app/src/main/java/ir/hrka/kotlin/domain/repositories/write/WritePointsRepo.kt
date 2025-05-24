package ir.hrka.kotlin.domain.repositories.write

import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.domain.entities.db.DBPoint
import kotlinx.coroutines.flow.Flow

interface WritePointsRepo {

    suspend fun savePoint(point: DBPoint): Flow<Result<Long?, BaseError>>
    suspend fun removePoint(id: Long): Flow<Result<Boolean?, BaseError>>
}