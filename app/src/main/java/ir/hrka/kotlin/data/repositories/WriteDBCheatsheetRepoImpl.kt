package ir.hrka.kotlin.data.repositories

import ir.hrka.kotlin.core.Constants.DB_CLEAR_CHEATSHEET_TABLE_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_DELETE_CHEATSHEET_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_UPDATE_CHEATSHEETS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_CHEATSHEETS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_SNIPPET_CODES_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_SUB_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.CheatsheetDao
import ir.hrka.kotlin.data.datasource.PointDao
import ir.hrka.kotlin.data.datasource.SnippetCodeDao
import ir.hrka.kotlin.data.datasource.SubPointDao
import ir.hrka.kotlin.domain.entities.ErrorModel
import ir.hrka.kotlin.domain.entities.db.Cheatsheet
import ir.hrka.kotlin.domain.entities.db.Point
import ir.hrka.kotlin.domain.entities.db.SnippetCode
import ir.hrka.kotlin.domain.entities.db.SubPoint
import ir.hrka.kotlin.domain.repositories.WriteCheatsheetRepo
import javax.inject.Inject

class WriteDBCheatsheetRepoImpl @Inject constructor(
    private val cheatsheetDao: CheatsheetDao,
    private val pointDao: PointDao,
    private val subPointDao: SubPointDao,
    private val snippetCodeDao: SnippetCodeDao
) : WriteCheatsheetRepo {

    override suspend fun saveCheatSheetsOnDB(cheatsheets: List<Cheatsheet>): Resource<Boolean> {
        return try {
            cheatsheetDao.insertCheatsheets(*cheatsheets.toTypedArray())
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                ErrorModel(
                    errorCode = DB_WRITE_CHEATSHEETS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun clearCheatsheetTable(): Resource<Boolean> {
        return try {
            cheatsheetDao.deleteCheatsheets()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                ErrorModel(
                    errorCode = DB_CLEAR_CHEATSHEET_TABLE_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun updateCheatsheetStateOnDB(
        id: Int,
        hasContentUpdated: Boolean
    ): Resource<Boolean> {
        return try {
            cheatsheetDao.updateCheatsheetUpdateState(id, hasContentUpdated)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                ErrorModel(
                    errorCode = DB_UPDATE_CHEATSHEETS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun savePointsOnDB(point: Point): Resource<Long> {
        return try {
            val rowId = pointDao.insertPoints(point)
            Resource.Success(rowId)
        } catch (e: Exception) {
            Resource.Error(
                ErrorModel(
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
                ErrorModel(
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
                ErrorModel(
                    errorCode = DB_WRITE_SNIPPET_CODES_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun deleteCheatsheetPoints(cheatsheetName: String): Resource<Boolean> {
        return try {
            pointDao.deleteCheatsheetPoints(cheatsheetName)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                ErrorModel(
                    errorCode = DB_DELETE_CHEATSHEET_POINTS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }
}