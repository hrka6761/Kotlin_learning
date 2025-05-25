package ir.hrka.kotlin.data.repositories.db.write

import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.core.utilities.handleDBResponse
import ir.hrka.kotlin.data.datasource.db.interactions.SubPointDao
import ir.hrka.kotlin.domain.entities.db.SubPoint
import ir.hrka.kotlin.domain.repositories.write.WriteSubPointsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WriteDBSubPointsRepoImpl @Inject constructor(
    private val subPointDao: SubPointDao
) : WriteSubPointsRepo {

    override suspend fun saveSubPoints(subPoints: Array<SubPoint>): Flow<Result<Boolean?, Errors>> =
        handleDBResponse {
            subPointDao.insertSubPoints(*subPoints)
            return@handleDBResponse true
        }

    override suspend fun removeSubPoints(pointId: Long): Flow<Result<Boolean?, Errors>> =
        handleDBResponse {
            subPointDao.deleteSubPoints(pointId)
            return@handleDBResponse true
        }
}