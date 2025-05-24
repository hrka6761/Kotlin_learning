package ir.hrka.kotlin.domain.repositories.write

import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.domain.entities.db.SubPoint
import kotlinx.coroutines.flow.Flow

interface WriteSubPointsRepo {

    suspend fun saveSubPoints(subPoints: Array<SubPoint>): Flow<Result<Boolean?, BaseError>>
    suspend fun removeSubPoints(pointId: Long): Flow<Result<Boolean?, BaseError>>
}