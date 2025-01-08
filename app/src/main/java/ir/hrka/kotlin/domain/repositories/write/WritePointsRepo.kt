package ir.hrka.kotlin.domain.repositories.write

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.DBPoint

interface WritePointsRepo {

    suspend fun savePoint(point: DBPoint): Resource<Long?>
    suspend fun removePoint(id: Long): Resource<Boolean?>
}