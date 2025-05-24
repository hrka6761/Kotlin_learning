package ir.hrka.kotlin.data.repositories.db.write

import ir.hrka.kotlin.core.Constants.DB_REMOVE_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.data.datasource.db.interactions.PointDao
import ir.hrka.kotlin.domain.entities.db.DBPoint
import ir.hrka.kotlin.domain.repositories.write.WritePointsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WriteDBPointsRepoImpl @Inject constructor(private val pointDao: PointDao) : WritePointsRepo {

    override suspend fun savePoint(point: DBPoint): Flow<Result<Long?, BaseError>> =
        flow {
            emit(Result.Loading)

            try {
                val rowId = pointDao.insertPoints(point)
                emit(Result.Success(rowId))
            } catch (e: Exception) {
                emit(
                    Result.Error(
                        Error(
                            errorCode = DB_WRITE_POINTS_ERROR_CODE,
                            errorMsg = e.message.toString()
                        )
                    )
                )
            }
        }


    override suspend fun removePoint(id: Long): Flow<Result<Boolean?, BaseError>> =
        flow {
            emit(Result.Loading)

            try {
                pointDao.deletePoints(id)
                emit(Result.Success(true))
            } catch (e: Exception) {
                emit(
                    Result.Error(
                        Error(
                            errorCode = DB_REMOVE_POINTS_ERROR_CODE,
                            errorMsg = e.message.toString()
                        )
                    )
                )
            }
        }
}