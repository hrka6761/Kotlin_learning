package ir.hrka.kotlin.data.repositories.db.write

import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.core.utilities.handleDBResponse
import ir.hrka.kotlin.data.datasource.db.interactions.PointDao
import ir.hrka.kotlin.domain.entities.db.DBPoint
import ir.hrka.kotlin.domain.repositories.write.WritePointsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WriteDBPointsRepoImpl @Inject constructor(private val pointDao: PointDao) : WritePointsRepo {

    override suspend fun savePoint(DBPoint: DBPoint): Flow<Result<Long?, Errors>> =
        handleDBResponse { pointDao.insertPoints(DBPoint) }

    override suspend fun removePoint(id: Long): Flow<Result<Boolean?, Errors>> =
        handleDBResponse {
            pointDao.deletePoints(id)
            return@handleDBResponse true
        }
}