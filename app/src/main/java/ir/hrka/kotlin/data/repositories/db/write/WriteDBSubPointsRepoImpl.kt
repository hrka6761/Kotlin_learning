package ir.hrka.kotlin.data.repositories.db.write

import ir.hrka.kotlin.core.Constants.DB_REMOVE_SUB_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_SUB_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.SubPointDao
import ir.hrka.kotlin.domain.entities.db.SubPoint
import ir.hrka.kotlin.domain.repositories.write.WriteSubPointsRepo
import javax.inject.Inject

class WriteDBSubPointsRepoImpl @Inject constructor(
    private val subPointDao: SubPointDao
) : WriteSubPointsRepo {

    override suspend fun saveSubPoints(subPoints: Array<SubPoint>): Resource<Boolean?> {
        return try {
            subPointDao.insertSubPoints(*subPoints)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_WRITE_SUB_POINTS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun removeSubPoints(pointId: Long): Resource<Boolean?> {
        return try {
            subPointDao.deleteSubPoints(pointId)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_REMOVE_SUB_POINTS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }
}