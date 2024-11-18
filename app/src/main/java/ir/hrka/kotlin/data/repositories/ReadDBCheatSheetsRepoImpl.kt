package ir.hrka.kotlin.data.repositories

import ir.hrka.kotlin.core.utilities.Constants.DB_READ_POINT_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.CheatsheetDao
import ir.hrka.kotlin.domain.entities.ErrorModel
import ir.hrka.kotlin.domain.entities.RepoFileModel
import ir.hrka.kotlin.domain.repositories.CheatSheetsRepo
import javax.inject.Inject

class ReadDBCheatSheetsRepoImpl @Inject constructor(
    private val cheatsheetDao: CheatsheetDao
) : CheatSheetsRepo {

    override suspend fun getCheatSheetsList(): Resource<List<RepoFileModel>?> {
        return try {
            val cheatsheets = cheatsheetDao.getCheatsheets().let {
                it.map { dbCheatsheet ->
                    val repoFileModel = RepoFileModel(
                        name = dbCheatsheet.title,
                        path = "",
                        sha = "",
                        size = -1,
                        htmlUrl = "",
                        url = "",
                        gitUrl = "",
                        downloadUrl = "",
                        type = "",
                        content = null,
                        encoding = null,
                        linksModel = null
                    )
                    repoFileModel.hasContentUpdated = dbCheatsheet.hasContentUpdated

                    return@map repoFileModel
                }
            }
            Resource.Success(cheatsheets)
        } catch (e: Exception) {
            Resource.Error(
                ErrorModel(
                    errorCode = DB_READ_POINT_ERROR_CODE,
                    errorMsg = "Can't read cheatsheet from the database."
                )
            )
        }
    }

    override suspend fun getCheatSheetContent(fileName: String): Resource<RepoFileModel?> {
        return Resource.Initial()
    }
}