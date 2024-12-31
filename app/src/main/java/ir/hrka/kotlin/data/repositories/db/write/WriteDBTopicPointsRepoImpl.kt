package ir.hrka.kotlin.data.repositories.db.write

import ir.hrka.kotlin.core.Constants.DB_DELETE_KOTLIN_TOPICS_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_DELETE_POINT_SNIPPET_CODES_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_DELETE_POINT_SUB_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_SNIPPET_CODES_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_SUB_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.PointsDao
import ir.hrka.kotlin.data.datasource.db.interactions.SnippetCodesDao
import ir.hrka.kotlin.data.datasource.db.interactions.SubPointsDao
import ir.hrka.kotlin.domain.entities.db.Point
import ir.hrka.kotlin.domain.entities.db.SnippetCode
import ir.hrka.kotlin.domain.entities.db.SubPoint
import ir.hrka.kotlin.domain.repositories.db.write.WriteDBTopicPointsRepo
import javax.inject.Inject

class WriteDBTopicPointsRepoImpl @Inject constructor(
    private val pointDao: PointsDao,
    private val subPointDao: SubPointsDao,
    private val snippetCodeDao: SnippetCodesDao
) : WriteDBTopicPointsRepo {

    override suspend fun savePointOnDB(point: Point): Resource<Long> {
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

    override suspend fun saveSubPointsOnDB(subPoints: Array<SubPoint>): Resource<Boolean> {
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

    override suspend fun saveSnippetCodesOnDB(snippetCodes: Array<SnippetCode>): Resource<Boolean> {
        return try {
            snippetCodeDao.insertSnippetCodes(*snippetCodes)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_WRITE_SNIPPET_CODES_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun deletePoints(topicName: String): Resource<Boolean> {
        return try {
            pointDao.deletePoints(topicName)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_DELETE_KOTLIN_TOPICS_POINTS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun deleteSubPoints(pointId: Long): Resource<Boolean> {
        return try {
            subPointDao.deleteSubPoints(pointId)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_DELETE_POINT_SUB_POINTS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun deleteSnippetCodes(pointId: Long): Resource<Boolean> {
        return try {
            snippetCodeDao.deleteSnippetCodes(pointId)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_DELETE_POINT_SNIPPET_CODES_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }
}