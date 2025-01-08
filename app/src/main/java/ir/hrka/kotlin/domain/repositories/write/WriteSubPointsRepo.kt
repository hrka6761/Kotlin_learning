package ir.hrka.kotlin.domain.repositories.write

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.SubPoint

interface WriteSubPointsRepo {

    suspend fun saveSubPoints(subPoints: Array<SubPoint>): Resource<Boolean?>
    suspend fun removeSubPoints(pointId: Long): Resource<Boolean?>
}