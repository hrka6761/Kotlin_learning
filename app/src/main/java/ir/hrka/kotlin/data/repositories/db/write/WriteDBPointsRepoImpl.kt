package ir.hrka.kotlin.data.repositories.db.write

import ir.hrka.kotlin.core.Constants.DB_REMOVE_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.PointDao
import ir.hrka.kotlin.domain.entities.db.DBPoint
import ir.hrka.kotlin.domain.repositories.write.WritePointsRepo
import javax.inject.Inject

class WriteDBPointsRepoImpl @Inject constructor(private val pointDao: PointDao) : WritePointsRepo {

    override suspend fun savePoint(point: DBPoint): Resource<Long?> {
        return try {
            val rowId = pointDao.insertPoints(point)
            Resource.Success(rowId)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_WRITE_POINTS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun removePoint(id: Long): Resource<Boolean?> {
        return try {
            pointDao.deletePoints(id)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_REMOVE_POINTS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }
}