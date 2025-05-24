package ir.hrka.kotlin.data.repositories.db.write

import ir.hrka.kotlin.core.Constants.DB_REMOVE_SUB_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_SUB_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.data.datasource.db.interactions.SubPointDao
import ir.hrka.kotlin.domain.entities.db.SubPoint
import ir.hrka.kotlin.domain.repositories.write.WriteSubPointsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WriteDBSubPointsRepoImpl @Inject constructor(
    private val subPointDao: SubPointDao
) : WriteSubPointsRepo {

    override suspend fun saveSubPoints(subPoints: Array<SubPoint>): Flow<Result<Boolean?, BaseError>> =
        flow {
            emit(Result.Loading)

            try {
                subPointDao.insertSubPoints(*subPoints)
                emit(Result.Success(true))
            } catch (e: Exception) {
                emit(
                    Result.Error(
                        Error(
                            errorCode = DB_WRITE_SUB_POINTS_ERROR_CODE,
                            errorMsg = e.message.toString()
                        )
                    )
                )
            }
        }


    override suspend fun removeSubPoints(pointId: Long): Flow<Result<Boolean?, BaseError>> =
        flow {
            emit(Result.Loading)

            try {
                subPointDao.deleteSubPoints(pointId)
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(
                    Error(
                        errorCode = DB_REMOVE_SUB_POINTS_ERROR_CODE,
                        errorMsg = e.message.toString()
                    )
                )
            }
        }
}