package ir.hrka.kotlin.data.repositories

import ir.hrka.kotlin.core.utilities.Constants.RETROFIT_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.GithubAPI
import ir.hrka.kotlin.domain.entities.ErrorModel
import ir.hrka.kotlin.domain.entities.RepoFileModel
import ir.hrka.kotlin.domain.repositories.CheatSheetsRepo
import javax.inject.Inject

class ReadGithubCheatSheetsRepoImpl @Inject constructor(
    private val githubAPI: GithubAPI
) : CheatSheetsRepo {

    override suspend fun getCheatSheetsList(): Resource<List<RepoFileModel>?> {
        return try {
            val response = githubAPI.getCheatSheetsList()

            if (response.isSuccessful) {
                Resource.Success(response.body())
            } else {
                Resource.Error(
                    ErrorModel(
                        errorCode = response.code(),
                        errorMsg = response.message()
                    )
                )
            }
        } catch (e: Exception) {
            Resource.Error(
                ErrorModel(
                    errorCode = RETROFIT_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun getCheatSheetContent(fileName: String): Resource<RepoFileModel?> {
        return try {
            val response = githubAPI.getCheatSheetFile(fileName = fileName)

            if (response.isSuccessful) {
                Resource.Success(response.body())
            } else {
                Resource.Error(
                    ErrorModel(
                        errorCode = response.code(),
                        errorMsg = response.message()
                    )
                )
            }
        } catch (e: Exception) {
            Resource.Error(
                ErrorModel(
                    errorCode = RETROFIT_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }
}