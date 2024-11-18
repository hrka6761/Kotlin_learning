package ir.hrka.kotlin.data.repositories

import ir.hrka.kotlin.core.utilities.Constants.DB_CLEAR_CHEATSHEETS_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Constants.DB_UPDATE_CHEATSHEETS_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Constants.DB_WRITE_CHEATSHEETS_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.CheatsheetDao
import ir.hrka.kotlin.domain.entities.ErrorModel
import ir.hrka.kotlin.domain.entities.db.Cheatsheet
import ir.hrka.kotlin.domain.repositories.DBRepo
import javax.inject.Inject

class WriteDBCheatSheetsRepoImpl @Inject constructor(private val cheatsheetDao: CheatsheetDao) :
    DBRepo {

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

    override suspend fun clearDB(): Resource<Boolean> {
        return try {
            cheatsheetDao.deleteCheatsheets()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                ErrorModel(
                    errorCode = DB_CLEAR_CHEATSHEETS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun updateCheatsheetUpdateStateOnDB(
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
}